package kz.bitlab.mainservice.dto.chapter.request;

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
@Schema(description = "DTO для обновления существующей главы")
public class ChapterUpdateDTO {
    @NotNull(message = "ID главы не может быть пустым")
    @Schema(description = "Идентификатор главы", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Size(min = 2, max = 255, message = "Название главы должно содержать от 2 до 255 символов")
    @Schema(description = "Название главы", example = "Обновленное введение в Java")
    private String name;

    @Size(max = 1000, message = "Описание главы не должно превышать 1000 символов")
    @Schema(description = "Описание главы", example = "Обновленное знакомство с основами Java")
    private String description;

    @Schema(description = "Порядковый номер главы", example = "2")
    private Integer order;
}