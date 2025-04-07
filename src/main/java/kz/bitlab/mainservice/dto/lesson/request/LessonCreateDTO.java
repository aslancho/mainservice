package kz.bitlab.mainservice.dto.lesson.request;

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
@Schema(description = "DTO для создания нового урока")
public class LessonCreateDTO {

    @Size(min = 2, max = 255, message = "Название урока должно содержать от 2 до 255 символов")
    @Schema(description = "Название урока", example = "Установка JDK и IDE")
    private String name;

    @Size(max = 1000, message = "Описание урока не должно превышать 1000 символов")
    @Schema(description = "Описание урока", example = "Руководство по установке Java Development Kit и IntelliJ IDEA")
    private String description;

    @Schema(description = "Содержание урока", example = "В этом уроке мы узнаем, как установить необходимые инструменты для разработки на Java.")
    private String content;

    @NotNull(message = "Порядковый номер урока обязателен")
    @Schema(description = "Порядковый номер урока", example = "1")
    private Integer order;

    @NotNull(message = "ID главы обязателен")
    @Schema(description = "Идентификатор главы, к которой относится урок", example = "1")
    private Long chapterId;
}