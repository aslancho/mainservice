package kz.bitlab.mainservice.dto.chapter.response;

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
@Schema(description = "Информация о главе")
public class ChapterResponseDTO {
    @Schema(description = "Уникальный идентификатор главы")
    private Long id;

    @Schema(description = "Название главы")
    private String name;

    @Schema(description = "Описание главы")
    private String description;

    @Schema(description = "Порядковый номер главы")
    private Integer order;

    @Schema(description = "Идентификатор курса")
    private Long courseId;

    @Schema(description = "Дата и время создания главы")
    private LocalDateTime createdTime;

    @Schema(description = "Дата и время последнего обновления главы")
    private LocalDateTime updatedTime;
}