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
@Schema(description = "DTO для обновления существующего курса")
public class CourseUpdateDTO {

    @Schema(description = "Идентификатор курса", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "Название курса", example = "Java Developer Advanced")
    private String name;

    @Schema(description = "Описание курса", example = "Обновленный курс по Java разработке с углубленным изучением фреймворков")
    private String description;
}