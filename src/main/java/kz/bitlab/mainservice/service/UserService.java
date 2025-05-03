package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.user.request.UserCreateDTO;
import kz.bitlab.mainservice.dto.user.request.UserUpdateDTO;
import kz.bitlab.mainservice.dto.user.response.UserResponseDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO userCreateDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String userId);

    void deleteUser(String userId);

    UserResponseDTO updateUser(String userId, UserUpdateDTO updateDTO, boolean isAdmin);

    String getCurrentUserId();

    boolean isCurrentUserAdmin();

    Set<String> getUserRoles(String userId);

    UserResponseDTO mapToUserResponseDTO(UserRepresentation user, Set<String> roles);
}
