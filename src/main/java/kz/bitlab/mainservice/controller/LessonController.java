package kz.bitlab.mainservice.controller;

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
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        List<LessonResponseDTO> lessonResponseDTOs = lessonService.getAllLessons();
        return ResponseEntity.ok(lessonResponseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable Long id) {
        LessonResponseDTO lessonResponseDTO = lessonService.getLessonById(id);
        return ResponseEntity.ok(lessonResponseDTO);
    }

    @PostMapping
    public ResponseEntity<LessonResponseDTO> createLesson(@Valid @RequestBody LessonCreateDTO lessonCreateDTO) {
        LessonResponseDTO savedLesson = lessonService.createLesson(lessonCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> updateLesson(@PathVariable Long id, @Valid @RequestBody LessonUpdateDTO lessonUpdateDTO) {
        // Установка id из пути в DTO, если он не был установлен
        lessonUpdateDTO.setId(id);

        LessonResponseDTO updatedLesson = lessonService.updateLesson(lessonUpdateDTO);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<LessonResponseDTO>> getLessonsByChapterId(@PathVariable Long chapterId) {
        List<LessonResponseDTO> lessonResponseDTOs = lessonService.getLessonsByChapterId(chapterId);
        return ResponseEntity.ok(lessonResponseDTOs);
    }
}