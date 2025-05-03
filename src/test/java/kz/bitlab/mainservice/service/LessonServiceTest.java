package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.lesson.request.LessonCreateDTO;
import kz.bitlab.mainservice.dto.lesson.request.LessonUpdateDTO;
import kz.bitlab.mainservice.dto.lesson.response.LessonResponseDTO;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.entity.Lesson;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.mapper.LessonMapper;
import kz.bitlab.mainservice.repository.ChapterRepository;
import kz.bitlab.mainservice.repository.LessonRepository;
import kz.bitlab.mainservice.service.impl.LessonServiceImpl;
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
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Chapter chapter;
    private Lesson lesson;
    private LessonResponseDTO lessonResponseDTO;
    private LessonCreateDTO lessonCreateDTO;
    private LessonUpdateDTO lessonUpdateDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // Подготовка тестовых данных - Course Entity
        Course course = Course.builder()
                .id(1L)
                .name("Java Developer")
                .description("Полный курс по Java разработке")
                .createdTime(now)
                .updatedTime(now)
                .build();

        // Подготовка тестовых данных - Chapter Entity
        chapter = Chapter.builder()
                .id(1L)
                .name("Введение в Java")
                .description("Знакомство с Java")
                .order(1)
                .course(course)
                .createdTime(now)
                .updatedTime(now)
                .build();

        // Подготовка тестовых данных - Lesson Entity
        lesson = Lesson.builder()
                .id(1L)
                .name("Установка JDK")
                .description("Установка Java Development Kit")
                .content("Подробное руководство по установке JDK")
                .order(1)
                .chapter(chapter)
                .createdTime(now)
                .updatedTime(now)
                .build();

        // Подготовка тестовых данных - Response DTO
        lessonResponseDTO = LessonResponseDTO.builder()
                .id(1L)
                .name("Установка JDK")
                .description("Установка Java Development Kit")
                .content("Подробное руководство по установке JDK")
                .order(1)
                .chapterId(1L)
                .createdTime(now)
                .updatedTime(now)
                .build();

        // Подготовка тестовых данных - Create DTO
        lessonCreateDTO = LessonCreateDTO.builder()
                .name("Установка JDK")
                .description("Установка Java Development Kit")
                .content("Подробное руководство по установке JDK")
                .order(1)
                .chapterId(1L)
                .build();

        // Подготовка тестовых данных - Update DTO
        lessonUpdateDTO = LessonUpdateDTO.builder()
                .id(1L)
                .name("Обновленная установка JDK")
                .description("Обновленное руководство по установке JDK")
                .content("Обновленное подробное руководство по установке JDK")
                .order(2)
                .build();
    }

    @Test
    void getAllLessons_ShouldReturnListOfLessonResponseDTO() {
        // Arrange
        List<Lesson> lessons = Collections.singletonList(lesson);

        when(lessonRepository.findAll()).thenReturn(lessons);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(lessonResponseDTO);

        // Act
        List<LessonResponseDTO> result = lessonService.getAllLessons();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(lessonResponseDTO, result.get(0));

        verify(lessonRepository).findAll();
        verify(lessonMapper).toResponseDto(lesson);
    }

    @Test
    void getLessonById_ExistingId_ShouldReturnLessonResponseDTO() {
        // Arrange
        Long lessonId = 1L;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(lessonResponseDTO);

        // Act
        LessonResponseDTO result = lessonService.getLessonById(lessonId);

        // Assert
        assertNotNull(result);
        assertEquals(lessonResponseDTO, result);

        verify(lessonRepository).findById(lessonId);
        verify(lessonMapper).toResponseDto(lesson);
    }

    @Test
    void getLessonById_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long lessonId = 99L;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.getLessonById(lessonId));

        assertTrue(exception.getMessage().contains("Lesson with id " + lessonId + " not found"));
        verify(lessonRepository).findById(lessonId);
        verify(lessonMapper, never()).toResponseDto(any(Lesson.class));
    }

    @Test
    void getLessonsByChapterId_ExistingChapterId_ShouldReturnListOfLessonResponseDTO() {
        // Arrange
        Long chapterId = 1L;
        Lesson lessonWithChapter = Lesson.builder()
                .id(1L)
                .chapter(Chapter.builder().id(chapterId).build())
                .build();
        List<Lesson> lessons = Collections.singletonList(lessonWithChapter);

        when(chapterRepository.existsById(chapterId)).thenReturn(true);
        when(lessonRepository.findAll()).thenReturn(lessons);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(lessonResponseDTO);

        // Act
        List<LessonResponseDTO> result = lessonService.getLessonsByChapterId(chapterId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(lessonResponseDTO, result.get(0));

        verify(chapterRepository).existsById(chapterId);
        verify(lessonRepository).findAll();
        verify(lessonMapper).toResponseDto(any(Lesson.class));
    }

    @Test
    void getLessonsByChapterId_NonExistingChapterId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long chapterId = 99L;

        when(chapterRepository.existsById(chapterId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.getLessonsByChapterId(chapterId));

        assertTrue(exception.getMessage().contains("Chapter with id " + chapterId + " not found"));
        verify(chapterRepository).existsById(chapterId);
        verify(lessonRepository, never()).findAll();
    }

    @Test
    void createLesson_ValidInput_ShouldReturnCreatedLessonResponseDTO() {
        // Arrange
        Long chapterId = 1L;

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonMapper.toEntity(any(LessonCreateDTO.class))).thenReturn(lesson);
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(lessonResponseDTO);

        // Act
        LessonResponseDTO result = lessonService.createLesson(lessonCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(lessonResponseDTO, result);

        verify(chapterRepository).findById(chapterId);
        verify(lessonMapper).toEntity(lessonCreateDTO);
        verify(lessonRepository).save(any(Lesson.class));
        verify(lessonMapper).toResponseDto(lesson);
    }

    @Test
    void createLesson_NonExistingChapterId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long chapterId = 99L;
        lessonCreateDTO.setChapterId(chapterId);

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.createLesson(lessonCreateDTO));

        assertTrue(exception.getMessage().contains("Chapter with id " + chapterId + " not found"));
        verify(chapterRepository).findById(chapterId);
        verify(lessonMapper, never()).toEntity(any(LessonCreateDTO.class));
        verify(lessonRepository, never()).save(any(Lesson.class));
    }

    @Test
    void updateLesson_ExistingId_ShouldReturnUpdatedLessonResponseDTO() {
        // Arrange
        Long lessonId = 1L;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(lessonResponseDTO);

        // Act
        LessonResponseDTO result = lessonService.updateLesson(lessonUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(lessonResponseDTO, result);

        verify(lessonRepository).findById(lessonId);
        verify(lessonRepository).save(lesson);
        verify(lessonMapper).toResponseDto(lesson);
    }

    @Test
    void updateLesson_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long lessonId = 99L;
        lessonUpdateDTO.setId(lessonId);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.updateLesson(lessonUpdateDTO));

        assertTrue(exception.getMessage().contains("Lesson with id " + lessonId + " not found"));
        verify(lessonRepository).findById(lessonId);
        verify(lessonRepository, never()).save(any(Lesson.class));
    }

    @Test
    void deleteLesson_ExistingId_ShouldDeleteLesson() {
        // Arrange
        Long lessonId = 1L;

        when(lessonRepository.existsById(lessonId)).thenReturn(true);
        doNothing().when(lessonRepository).deleteById(lessonId);

        // Act
        lessonService.deleteLesson(lessonId);

        // Assert
        verify(lessonRepository).existsById(lessonId);
        verify(lessonRepository).deleteById(lessonId);
    }

    @Test
    void deleteLesson_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long lessonId = 99L;

        when(lessonRepository.existsById(lessonId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> lessonService.deleteLesson(lessonId));

        assertTrue(exception.getMessage().contains("Lesson with id " + lessonId + " not found"));
        verify(lessonRepository).existsById(lessonId);
        verify(lessonRepository, never()).deleteById(any());
    }
}