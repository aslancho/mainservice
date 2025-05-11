package kz.bitlab.mainservice.dto.attachment.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(description = "DTO для создания нового вложения")
public class AttachmentCreateDTO {

    @NotNull(message = "ID урока обязателен")
    @Schema(description = "Идентификатор урока, к которому относится вложение", example = "1")
    private Long lessonId;

    @NotNull(message = "Файл обязателен")
    @Schema(description = "Файл для загрузки")
    private MultipartFile file;
}