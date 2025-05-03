package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.mainservice.dto.user.request.UserCreateDTO;
import kz.bitlab.mainservice.dto.user.request.UserUpdateDTO;
import kz.bitlab.mainservice.dto.user.response.UserResponseDTO;
import kz.bitlab.mainservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя в системе. Доступно только для администраторов.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        log.info("REST request to create a new user: {}", userCreateDTO.getUsername());
        UserResponseDTO createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей. Доступно только для администраторов.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("REST request to get all users");
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает пользователя по указанному идентификатору. Доступно только для администраторов.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        log.info("REST request to get user : {}", id);
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по указанному идентификатору. Доступно только для администраторов.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        log.info("REST request to delete user : {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Обновить данные пользователя",
            description = "Обновляет данные пользователя по указанному идентификатору. Пользователи могут обновлять только свои данные, администраторы могут обновлять данные любого пользователя и их роли.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные пользователя"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("REST request to update user: {}", id);
        boolean isAdmin = userService.isCurrentUserAdmin();
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO, isAdmin);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Получить данные текущего пользователя",
            description = "Возвращает данные текущего аутентифицированного пользователя.",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные пользователя успешно получены"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getCurrentUserProfile() {
        log.info("REST request to get current user profile");
        String currentUserId = userService.getCurrentUserId();
        UserResponseDTO user = userService.getUserById(currentUserId);
        return ResponseEntity.ok(user);
    }
}