package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.mainservice.dto.chapter.request.ChapterCreateDTO;
import kz.bitlab.mainservice.dto.chapter.request.ChapterUpdateDTO;
import kz.bitlab.mainservice.dto.chapter.response.ChapterResponseDTO;
import kz.bitlab.mainservice.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
@RequiredArgsConstructor
@Tag(name = "Главы", description = "API для работы с главами")
public class ChapterController {

    private final ChapterService chapterService;

    @Operation(summary = "Получить все главы", description = "Возвращает список всех доступных глав")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Главы успешно получены")
    })
    @GetMapping
    public ResponseEntity<List<ChapterResponseDTO>> getAllChapters() {
        List<ChapterResponseDTO> chapterResponseDTOs = chapterService.getAllChapters();
        return ResponseEntity.ok(chapterResponseDTOs);
    }

    @Operation(summary = "Получить главу по ID", description = "Возвращает главу по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Глава успешно получена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ChapterResponseDTO> getChapterById(@PathVariable Long id) {
        ChapterResponseDTO chapterResponseDTO = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapterResponseDTO);
    }

    @Operation(summary = "Создать новую главу", description = "Создает новую главу и возвращает её с присвоенным ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Глава успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные главы"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PostMapping
    public ResponseEntity<ChapterResponseDTO> createChapter(@Valid @RequestBody ChapterCreateDTO chapterCreateDTO) {
        ChapterResponseDTO savedChapter = chapterService.createChapter(chapterCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChapter);
    }

    @Operation(summary = "Обновить главу", description = "Обновляет существующую главу по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Глава успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные главы")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ChapterResponseDTO> updateChapter(@PathVariable Long id, @Valid @RequestBody ChapterUpdateDTO chapterUpdateDTO) {
        // Установка id из пути в DTO, если он не был установлен
        chapterUpdateDTO.setId(id);

        ChapterResponseDTO updatedChapter = chapterService.updateChapter(chapterUpdateDTO);
        return ResponseEntity.ok(updatedChapter);
    }

    @Operation(summary = "Удалить главу", description = "Удаляет главу по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Глава успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить главы по ID курса", description = "Возвращает список всех глав для указанного курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Главы успешно получены"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ChapterResponseDTO>> getChaptersByCourseId(@PathVariable Long courseId) {
        List<ChapterResponseDTO> chapterResponseDTOs = chapterService.getChaptersByCourseId(courseId);
        return ResponseEntity.ok(chapterResponseDTOs);
    }
}