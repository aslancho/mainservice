package kz.bitlab.mainservice.dto.lesson.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "Информация об уроке")
public class LessonResponseDTO {
    @Schema(description = "Уникальный идентификатор урока")
    private Long id;

    @Schema(description = "Название урока")
    private String name;

    @Schema(description = "Описание урока")
    private String description;

    @Schema(description = "Содержание урока")
    private String content;

    @Schema(description = "Порядковый номер урока")
    private Integer order;

    @Schema(description = "Идентификатор главы")
    private Long chapterId;

    @Schema(description = "Дата и время создания урока")
    private LocalDateTime createdTime;

    @Schema(description = "Дата и время последнего обновления урока")
    private LocalDateTime updatedTime;
}