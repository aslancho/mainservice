package kz.bitlab.mainservice.controller;

import jakarta.validation.Valid;
import kz.bitlab.mainservice.dto.course.request.CourseCreateDTO;
import kz.bitlab.mainservice.dto.course.request.CourseUpdateDTO;
import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courseResponseDTOS = courseService.getAllCourses();
        return ResponseEntity.ok(courseResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        CourseResponseDTO courseResponseDTO = courseService.getCourseById(id);
        return ResponseEntity.ok(courseResponseDTO);
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseCreateDTO courseCreateDTO) {
        CourseResponseDTO savedCourse = courseService.createCourse(courseCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseUpdateDTO courseUpdateDTO) {
        // Установка id из пути в DTO, если он не был установлен
        courseUpdateDTO.setId(id);

        CourseResponseDTO updatedCourse = courseService.updateCourse(courseUpdateDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}