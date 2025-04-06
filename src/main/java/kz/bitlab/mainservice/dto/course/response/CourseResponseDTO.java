package kz.bitlab.mainservice.dto.course.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "Информация о курсе")
public class CourseResponseDTO {
    @Schema(description = "Уникальный идентификатор курса")
    private Long id;

    @Schema(description = "Название курса")
    private String name;

    @Schema(description = "Описание курса")
    private String description;

    @Schema(description = "Дата и время создания курса")
    private LocalDateTime createdTime;

    @Schema(description = "Дата и время последнего обновления курса")
    private LocalDateTime updatedTime;
}
