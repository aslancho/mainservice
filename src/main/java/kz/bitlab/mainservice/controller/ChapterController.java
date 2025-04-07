package kz.bitlab.mainservice.controller;

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
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping
    public ResponseEntity<List<ChapterResponseDTO>> getAllChapters() {
        List<ChapterResponseDTO> chapterResponseDTOs = chapterService.getAllChapters();
        return ResponseEntity.ok(chapterResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterResponseDTO> getChapterById(@PathVariable Long id) {
        ChapterResponseDTO chapterResponseDTO = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapterResponseDTO);
    }

    @PostMapping
    public ResponseEntity<ChapterResponseDTO> createChapter(@Valid @RequestBody ChapterCreateDTO chapterCreateDTO) {
        ChapterResponseDTO savedChapter = chapterService.createChapter(chapterCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChapter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChapterResponseDTO> updateChapter(@PathVariable Long id, @Valid @RequestBody ChapterUpdateDTO chapterUpdateDTO) {
        // Установка id из пути в DTO, если он не был установлен
        chapterUpdateDTO.setId(id);

        ChapterResponseDTO updatedChapter = chapterService.updateChapter(chapterUpdateDTO);
        return ResponseEntity.ok(updatedChapter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ChapterResponseDTO>> getChaptersByCourseId(@PathVariable Long courseId) {
        List<ChapterResponseDTO> chapterResponseDTOs = chapterService.getChaptersByCourseId(courseId);
        return ResponseEntity.ok(chapterResponseDTOs);
    }
}