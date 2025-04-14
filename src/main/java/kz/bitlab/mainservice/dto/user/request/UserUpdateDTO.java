package kz.bitlab.mainservice.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для обновления данных пользователя")
public class UserUpdateDTO {

    @Size(min = 3, max = 50, message = "Имя пользователя должно содержать от 3 до 50 символов")
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @Email(message = "Некорректный формат email")
    @Schema(description = "Email пользователя", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Имя пользователя", example = "John")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastName;

    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Schema(description = "Новый пароль пользователя (если требуется изменить)", example = "newSecurePassword123")
    private String password;

    @Schema(description = "Роли пользователя (может изменить только администратор)", example = "[\"USER\", \"TEACHER\"]")
    private Set<String> roles;
}