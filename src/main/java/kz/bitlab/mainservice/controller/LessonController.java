package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.mainservice.dto.lesson.request.LessonCreateDTO;
import kz.bitlab.mainservice.dto.lesson.request.LessonUpdateDTO;
import kz.bitlab.mainservice.dto.lesson.response.LessonResponseDTO;
import kz.bitlab.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@Tag(name = "Уроки", description = "API для работы с уроками")
public class LessonController {

    private final LessonService lessonService;

    @Operation(summary = "Получить все уроки", description = "Возвращает список всех доступных уроков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уроки успешно получены")
    })
    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        List<LessonResponseDTO> lessonResponseDTOs = lessonService.getAllLessons();
        return ResponseEntity.ok(lessonResponseDTOs);
    }

    @Operation(summary = "Получить урок по ID", description = "Возвращает урок по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Урок успешно получен"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable Long id) {
        LessonResponseDTO lessonResponseDTO = lessonService.getLessonById(id);
        return ResponseEntity.ok(lessonResponseDTO);
    }

    @Operation(summary = "Создать новый урок", description = "Создает новый урок и возвращает его с присвоенным ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Урок успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные урока"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @PostMapping
    public ResponseEntity<LessonResponseDTO> createLesson(@Valid @RequestBody LessonCreateDTO lessonCreateDTO) {
        LessonResponseDTO savedLesson = lessonService.createLesson(lessonCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLesson);
    }

    @Operation(summary = "Обновить урок", description = "Обновляет существующий урок по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Урок успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Урок не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные урока")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonUpdateDTO lessonUpdateDTO) {
        // Установка id из пути в DTO, если он не был установлен
        lessonUpdateDTO.setId(id);

        LessonResponseDTO updatedLesson = lessonService.updateLesson(lessonUpdateDTO);
        return ResponseEntity.ok(updatedLesson);
    }

    @Operation(summary = "Удалить урок", description = "Удаляет урок по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Урок успешно удален"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить уроки по ID главы", description = "Возвращает список всех уроков для указанной главы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Уроки успешно получены"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsByChapterId(@PathVariable Long chapterId) {
        List<LessonResponseDTO> lessonResponseDTOs = lessonService.getLessonsByChapterId(chapterId);
        return ResponseEntity.ok(lessonResponseDTOs);
    }
}