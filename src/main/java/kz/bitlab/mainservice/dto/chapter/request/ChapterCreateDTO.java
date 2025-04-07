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
@Schema(description = "DTO для создания новой главы")
public class ChapterCreateDTO {

    @Size(min = 2, max = 255, message = "Название главы должно содержать от 2 до 255 символов")
    @Schema(description = "Название главы", example = "Введение в Java")
    private String name;

    @Size(max = 1000, message = "Описание главы не должно превышать 1000 символов")
    @Schema(description = "Описание главы", example = "Знакомство с основами Java")
    private String description;

    @NotNull(message = "Порядковый номер главы обязателен")
    @Schema(description = "Порядковый номер главы", example = "1")
    private Integer order;

    @NotNull(message = "ID курса обязателен")
    @Schema(description = "Идентификатор курса, к которому относится глава", example = "1")
    private Long courseId;
}