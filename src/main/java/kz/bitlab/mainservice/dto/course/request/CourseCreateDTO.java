package kz.bitlab.mainservice.dto.course.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Название курса", example = "Java Developer")
    private String name;

    @Schema(description = "Описание курса", example = "Полный курс по Java разработке от основ до продвинутых тем")
    private String description;
}