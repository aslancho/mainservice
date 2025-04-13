package kz.bitlab.mainservice.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о пользователе")
public class UserResponseDTO {

    @Schema(description = "Идентификатор пользователя")
    private String id;

    @Schema(description = "Имя пользователя")
    private String username;

    @Schema(description = "Email пользователя")
    private String email;

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Признак активации пользователя")
    private boolean enabled;

    @Schema(description = "Роли пользователя")
    private Set<String> roles;
}