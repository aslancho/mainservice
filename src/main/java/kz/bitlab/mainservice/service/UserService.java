package kz.bitlab.mainservice.service;

import jakarta.ws.rs.core.Response;
import kz.bitlab.mainservice.dto.user.request.UserCreateDTO;
import kz.bitlab.mainservice.dto.user.response.UserResponseDTO;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    /**
     * Создает нового пользователя в Keycloak
     */
    public UserResponseDTO createUser(UserCreateDTO userCreateDTO) {
        log.info("Creating new user with username: {}", userCreateDTO.getUsername());

        // Получаем ресурс Realm
        RealmResource realmResource = keycloakAdmin.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Создаем пользователя
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setFirstName(userCreateDTO.getFirstName());
        user.setLastName(userCreateDTO.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        // Создаем пользователя в Keycloak
        Response response = usersResource.create(user);
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            log.error("Failed to create user in Keycloak. Status: {}", response.getStatus());
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
        }

        // Получаем ID созданного пользователя
        String userId = CreatedResponseUtil.getCreatedId(response);
        log.debug("User created with ID: {}", userId);

        // Устанавливаем пароль для пользователя
        UserResource userResource = usersResource.get(userId);
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(userCreateDTO.getPassword());
        userResource.resetPassword(passwordCred);

        // Назначаем роли пользователю
        if (userCreateDTO.getRoles() != null && !userCreateDTO.getRoles().isEmpty()) {
            List<RoleRepresentation> rolesToAdd = new ArrayList<>();

            for (String roleName : userCreateDTO.getRoles()) {
                RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
                if (role != null) {
                    rolesToAdd.add(role);
                }
            }

            userResource.roles().realmLevel().add(rolesToAdd);
        }

        // Возвращаем информацию о созданном пользователе
        UserRepresentation createdUser = userResource.toRepresentation();
        Set<String> assignedRoles = getUserRoles(userId);

        return mapToUserResponseDTO(createdUser, assignedRoles);
    }

    /**
     * Получает список всех пользователей
     */
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");

        RealmResource realmResource = keycloakAdmin.realm(realm);
        List<UserRepresentation> users = realmResource.users().list();

        return users.stream()
                .map(user -> {
                    Set<String> roles = getUserRoles(user.getId());
                    return mapToUserResponseDTO(user, roles);
                })
                .collect(Collectors.toList());
    }

    /**
     * Получает пользователя по ID
     */
    public UserResponseDTO getUserById(String userId) {
        log.info("Fetching user with ID: {}", userId);

        RealmResource realmResource = keycloakAdmin.realm(realm);
        try {
            UserRepresentation user = realmResource.users().get(userId).toRepresentation();
            Set<String> roles = getUserRoles(userId);
            return mapToUserResponseDTO(user, roles);
        } catch (Exception e) {
            log.error("User with ID {} not found", userId, e);
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
    }

    /**
     * Удаляет пользователя по ID
     */
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);

        RealmResource realmResource = keycloakAdmin.realm(realm);
        try {
            realmResource.users().get(userId).remove();
            log.debug("User with ID {} successfully deleted", userId);
        } catch (Exception e) {
            log.error("Failed to delete user with ID {}", userId, e);
            throw new ResourceNotFoundException("User with ID " + userId + " not found or could not be deleted");
        }
    }

    /**
     * Получает роли пользователя
     */
    private Set<String> getUserRoles(String userId) {
        try {
            RealmResource realmResource = keycloakAdmin.realm(realm);
            List<RoleRepresentation> roles = realmResource.users().get(userId).roles().realmLevel().listAll();

            return roles.stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            log.error("Failed to get roles for user with ID {}", userId, e);
            return Collections.emptySet();
        }
    }

    /**
     * Преобразует UserRepresentation в UserResponseDTO
     */
    private UserResponseDTO mapToUserResponseDTO(UserRepresentation user, Set<String> roles) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .enabled(user.isEnabled())
                .roles(roles)
                .build();
    }
}