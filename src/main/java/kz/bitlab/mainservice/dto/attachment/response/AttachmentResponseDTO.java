package kz.bitlab.mainservice.dto.attachment.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "Информация о вложении")
public class AttachmentResponseDTO {
    @Schema(description = "Уникальный идентификатор вложения")
    private Long id;

    @Schema(description = "Название вложения")
    private String name;

    @Schema(description = "URL для скачивания вложения")
    private String url;

    @Schema(description = "Идентификатор урока")
    private Long lessonId;

    @Schema(description = "Дата и время создания вложения")
    private LocalDateTime createdTime;
}