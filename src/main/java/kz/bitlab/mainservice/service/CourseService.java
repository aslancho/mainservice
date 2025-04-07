package kz.bitlab.mainservice.service;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.mainservice.dto.course.request.CourseCreateDTO;
import kz.bitlab.mainservice.dto.course.request.CourseUpdateDTO;
import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.mapper.CourseMapper;
import kz.bitlab.mainservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getAllCourses() {
        logger.info("Fetching all courses");
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseById(Long id) {
        logger.info("Fetching course with id: {}", id);
        return courseRepository.findById(id)
                .map(courseMapper::toResponseDto)
                .orElseThrow(() -> {
                    logger.error("Course with id {} not found", id);
                    return ResourceNotFoundException.courseNotFound(id);
                });
    }

    public CourseResponseDTO createCourse(CourseCreateDTO courseCreateDTO) {
        logger.info("Creating new course: {}", courseCreateDTO);

        // Преобразовать DTO в сущность
        Course course = courseMapper.toEntity(courseCreateDTO);

        // Установка времени создания и обновления
        LocalDateTime now = LocalDateTime.now();
        course.setCreatedTime(now);
        course.setUpdatedTime(now);

        // Сохранение в БД
        Course savedCourse = courseRepository.save(course);
        logger.debug("Created course: {}", savedCourse);

        return courseMapper.toResponseDto(savedCourse);
    }

    public CourseResponseDTO updateCourse(CourseUpdateDTO courseUpdateDTO) {
        Long courseId = courseUpdateDTO.getId();
        logger.info("Updating course with id: {}", courseId);

        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.error("Course with id {} not found during update", courseId);
                    return new EntityNotFoundException("Course not found with id: " + courseId);
                });

        // Обновляем только те поля, которые есть в DTO
        if (courseUpdateDTO.getName() != null) {
            existingCourse.setName(courseUpdateDTO.getName());
        }
        if (courseUpdateDTO.getDescription() != null) {
            existingCourse.setDescription(courseUpdateDTO.getDescription());
        }

        existingCourse.setUpdatedTime(LocalDateTime.now());
        Course savedCourse = courseRepository.save(existingCourse);
        logger.debug("Updated course: {}", savedCourse);

        return courseMapper.toResponseDto(savedCourse);
    }

    public void deleteCourse(Long id) {
        logger.info("Deleting course with id: {}", id);
        if (!courseRepository.existsById(id)) {
            logger.error("Course with id {} not found during deletion", id);
            throw new EntityNotFoundException("Course not found with id: " + id);
        }

        courseRepository.deleteById(id);
        logger.info("Deleted course with id: {}", id);
    }
}