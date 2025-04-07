package kz.bitlab.mainservice.dto.course.request;

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
@Schema(description = "DTO для обновления существующего курса")
public class CourseUpdateDTO {
    @NotNull(message = "ID курса не может быть пустым")
    @Schema(description = "Идентификатор курса", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Size(min = 2, max = 255, message = "Название курса должно содержать от 2 до 255 символов")
    @Schema(description = "Название курса", example = "Java Developer Advanced")
    private String name;

    @Size(max = 1000, message = "Описание курса не должно превышать 1000 символов")
    @Schema(description = "Описание курса", example = "Обновленный курс по Java разработке с углубленным изучением фреймворков")
    private String description;
}