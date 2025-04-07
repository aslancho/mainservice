package kz.bitlab.mainservice.controller;

import kz.bitlab.mainservice.dto.course.request.CourseCreateDTO;
import kz.bitlab.mainservice.dto.course.request.CourseUpdateDTO;
import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        logger.info("Fetching all courses");

        List<CourseResponseDTO> courseResponseDTOS = courseService.getAllCourses();
        return ResponseEntity.ok(courseResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        logger.info("Fetching course with id: {}", id);

        Optional<CourseResponseDTO> courseResponseDTO = courseService.getCourseById(id);
        return courseResponseDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseCreateDTO courseCreateDTO) {
        logger.info("Creating new course: {}", courseCreateDTO.getName());

        CourseResponseDTO savedCourse = courseService.createCourse(courseCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateDTO courseUpdateDTO) {
        logger.info("Updating course with id: {}", id);

        Optional<CourseResponseDTO> optionalCourse = courseService.getCourseById(id);

        if (optionalCourse.isEmpty()) {
            logger.warn("Course with id: {} not found", id);
            return ResponseEntity.notFound().build();
        }

        // Установка id из пути в DTO, если он не был установлен
        courseUpdateDTO.setId(id);

        CourseResponseDTO updatedCourse = courseService.updateCourse(courseUpdateDTO);
        logger.info("Course with id: {} was updated successfully", id);

        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        logger.info("Deleting course with id: {}", id);

        Optional<CourseResponseDTO> optionalCourse = courseService.getCourseById(id);

        if (optionalCourse.isEmpty()) {
            logger.warn("Course with id: {} not found", id);
            return ResponseEntity.notFound().build();
        }

        courseService.deleteCourse(id);
        logger.info("Course with id: {} was deleted successfully", id);

        return ResponseEntity.noContent().build();
    }
}