package kz.bitlab.mainservice.service;

import jakarta.persistence.EntityNotFoundException;
import kz.bitlab.mainservice.dto.course.request.CourseCreateDTO;
import kz.bitlab.mainservice.dto.course.request.CourseUpdateDTO;
import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.mapper.CourseMapper;
import kz.bitlab.mainservice.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private CourseResponseDTO courseResponseDTO;
    private CourseCreateDTO courseCreateDTO;
    private CourseUpdateDTO courseUpdateDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // Подготовка тестовых данных - Entity
        course = Course.builder()
                .id(1L)
                .name("Java Developer")
                .description("Полный курс по Java разработке")
                .createdTime(now)
                .updatedTime(now)
                .build();

        // Подготовка тестовых данных - Response DTO
        courseResponseDTO = CourseResponseDTO.builder()
                .id(1L)
                .name("Java Developer")
                .description("Полный курс по Java разработке")
                .createdTime(now)
                .updatedTime(now)
                .build();

        // Подготовка тестовых данных - Create DTO
        courseCreateDTO = CourseCreateDTO.builder()
                .name("Java Developer")
                .description("Полный курс по Java разработке")
                .build();

        // Подготовка тестовых данных - Update DTO
        courseUpdateDTO = CourseUpdateDTO.builder()
                .id(1L)
                .name("Updated Java Developer")
                .description("Обновленный курс по Java разработке")
                .build();
    }

    @Test
    void getAllCourses_ShouldReturnListOfCourseResponseDTO() {
        // Arrange
        List<Course> courses = Collections.singletonList(course);

        when(courseRepository.findAll()).thenReturn(courses);
        when(courseMapper.toResponseDto(any(Course.class))).thenReturn(courseResponseDTO);

        // Act
        List<CourseResponseDTO> result = courseService.getAllCourses();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(courseResponseDTO, result.get(0));

        verify(courseRepository).findAll();
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    void getCourseById_ExistingId_ShouldReturnCourseResponseDTO() {
        // Arrange
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toResponseDto(any(Course.class))).thenReturn(courseResponseDTO);

        // Act
        CourseResponseDTO result = courseService.getCourseById(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(courseResponseDTO, result);

        verify(courseRepository).findById(courseId);
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    void getCourseById_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long courseId = 99L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> courseService.getCourseById(courseId));

        assertTrue(exception.getMessage().contains("Course with id " + courseId + " not found"));
        verify(courseRepository).findById(courseId);
        verify(courseMapper, never()).toResponseDto(any(Course.class));
    }

    @Test
    void createCourse_ValidInput_ShouldReturnCreatedCourseResponseDTO() {
        // Arrange
        when(courseMapper.toEntity(any(CourseCreateDTO.class))).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponseDto(any(Course.class))).thenReturn(courseResponseDTO);

        // Act
        CourseResponseDTO result = courseService.createCourse(courseCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(courseResponseDTO, result);

        verify(courseMapper).toEntity(courseCreateDTO);
        verify(courseRepository).save(any(Course.class));
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    void updateCourse_ExistingId_ShouldReturnUpdatedCourseResponseDTO() {
        // Arrange
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toResponseDto(any(Course.class))).thenReturn(courseResponseDTO);

        // Act
        CourseResponseDTO result = courseService.updateCourse(courseUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(courseResponseDTO, result);

        verify(courseRepository).findById(courseId);
        verify(courseRepository).save(course);
        verify(courseMapper).toResponseDto(course);
    }

    @Test
    void updateCourse_NonExistingId_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long courseId = 99L;
        courseUpdateDTO.setId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> courseService.updateCourse(courseUpdateDTO));

        assertTrue(exception.getMessage().contains("Course not found with id: " + courseId));
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void deleteCourse_ExistingId_ShouldDeleteCourse() {
        // Arrange
        Long courseId = 1L;

        when(courseRepository.existsById(courseId)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(courseId);

        // Act
        courseService.deleteCourse(courseId);

        // Assert
        verify(courseRepository).existsById(courseId);
        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void deleteCourse_NonExistingId_ShouldThrowEntityNotFoundException() {
        // Arrange
        Long courseId = 99L;

        when(courseRepository.existsById(courseId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> courseService.deleteCourse(courseId));

        assertTrue(exception.getMessage().contains("Course not found with id: " + courseId));
        verify(courseRepository).existsById(courseId);
        verify(courseRepository, never()).deleteById(any());
    }
}