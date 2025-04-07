package kz.bitlab.mainservice.dto.course.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO для создания нового курса")
public class CourseCreateDTO {

    @Size(min = 2, max = 255, message = "Название курса должно содержать от 2 до 255 символов")
    @Schema(description = "Название курса", example = "Java Developer")
    private String name;

    @Size(max = 1000, message = "Описание курса не должно превышать 1000 символов")
    @Schema(description = "Описание курса", example = "Полный курс по Java разработке от основ до продвинутых тем")
    private String description;
}