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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional(readOnly = true)
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseResponseDTO getCourseById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponseDto)
                .orElseThrow(() -> ResourceNotFoundException.courseNotFound(id));
    }

    public CourseResponseDTO createCourse(CourseCreateDTO courseCreateDTO) {
        // Преобразовать DTO в сущность
        Course course = courseMapper.toEntity(courseCreateDTO);

        // Установка времени создания и обновления
        LocalDateTime now = LocalDateTime.now();
        course.setCreatedTime(now);
        course.setUpdatedTime(now);

        // Сохранение в БД
        Course savedCourse = courseRepository.save(course);

        return courseMapper.toResponseDto(savedCourse);
    }

    public CourseResponseDTO updateCourse(CourseUpdateDTO courseUpdateDTO) {
        Long courseId = courseUpdateDTO.getId();
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        // Обновляем только те поля, которые есть в DTO
        if (courseUpdateDTO.getName() != null) {
            existingCourse.setName(courseUpdateDTO.getName());
        }
        if (courseUpdateDTO.getDescription() != null) {
            existingCourse.setDescription(courseUpdateDTO.getDescription());
        }

        existingCourse.setUpdatedTime(LocalDateTime.now());
        Course savedCourse = courseRepository.save(existingCourse);

        return courseMapper.toResponseDto(savedCourse);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with id: " + id);
        }

        courseRepository.deleteById(id);
    }
}
