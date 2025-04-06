package kz.bitlab.mainservice.controller;

import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        logger.info("Fetching all courses");
        List<Course> courses = courseService.getAll();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        logger.info("Fetching course with id: {}", id);
        Optional<Course> course = courseService.getById(id);
        return course.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        logger.info("Creating new course: {}", course.getName());

        // Установка времени создания и обновления
        LocalDateTime now = LocalDateTime.now();
        course.setCreatedTime(now);
        course.setUpdatedTime(now);

        Course savedCourse = courseService.add(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        logger.info("Updating course with id: {}", id);

        Optional<Course> optionalCourse = courseService.getById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setName(courseDetails.getName());
            existingCourse.setDescription(courseDetails.getDescription());
            existingCourse.setUpdatedTime(LocalDateTime.now());

            Course updatedCourse = courseService.add(existingCourse);
            return ResponseEntity.ok(updatedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        logger.info("Deleting course with id: {}", id);

        Optional<Course> course = courseService.getById(id);
        if (course.isPresent()) {
            courseService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}