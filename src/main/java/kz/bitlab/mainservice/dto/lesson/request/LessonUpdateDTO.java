package kz.bitlab.mainservice.dto.lesson.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO для обновления существующего урока")
public class LessonUpdateDTO {
    @NotNull(message = "ID урока не может быть пустым")
    @Schema(description = "Идентификатор урока", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Size(min = 2, max = 255, message = "Название урока должно содержать от 2 до 255 символов")
    @Schema(description = "Название урока", example = "Обновленная установка JDK и IDE")
    private String name;

    @Size(max = 1000, message = "Описание урока не должно превышать 1000 символов")
    @Schema(description = "Описание урока", example = "Обновленное руководство по установке Java Development Kit и IntelliJ IDEA")
    private String description;

    @Schema(description = "Содержание урока", example = "Обновленное содержание урока")
    private String content;

    @Schema(description = "Порядковый номер урока", example = "2")
    private Integer order;
}