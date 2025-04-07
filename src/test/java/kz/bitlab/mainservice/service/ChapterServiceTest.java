package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.chapter.request.ChapterCreateDTO;
import kz.bitlab.mainservice.dto.chapter.request.ChapterUpdateDTO;
import kz.bitlab.mainservice.dto.chapter.response.ChapterResponseDTO;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.mapper.ChapterMapper;
import kz.bitlab.mainservice.repository.ChapterRepository;
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
class ChapterServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterMapper chapterMapper;

    @InjectMocks
    private ChapterService chapterService;

    private Course course;
    private Chapter chapter;
    private ChapterResponseDTO chapterResponseDTO;
    private ChapterCreateDTO chapterCreateDTO;
    private ChapterUpdateDTO chapterUpdateDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // Подготовка тестовых данных - Course Entity
        course = Course.builder()
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

        // Подготовка тестовых данных - Response DTO
        chapterResponseDTO = ChapterResponseDTO.builder()
                .id(1L)
                .name("Введение в Java")
                .description("Знакомство с Java")
                .order(1)
                .courseId(1L)
                .createdTime(now)
                .updatedTime(now)
                .build();

        // Подготовка тестовых данных - Create DTO
        chapterCreateDTO = ChapterCreateDTO.builder()
                .name("Введение в Java")
                .description("Знакомство с Java")
                .order(1)
                .courseId(1L)
                .build();

        // Подготовка тестовых данных - Update DTO
        chapterUpdateDTO = ChapterUpdateDTO.builder()
                .id(1L)
                .name("Обновленное введение в Java")
                .description("Обновленное знакомство с Java")
                .order(2)
                .build();
    }

    @Test
    void getAllChapters_ShouldReturnListOfChapterResponseDTO() {
        // Arrange
        List<Chapter> chapters = Collections.singletonList(chapter);

        when(chapterRepository.findAll()).thenReturn(chapters);
        when(chapterMapper.toResponseDto(any(Chapter.class))).thenReturn(chapterResponseDTO);

        // Act
        List<ChapterResponseDTO> result = chapterService.getAllChapters();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chapterResponseDTO, result.get(0));

        verify(chapterRepository).findAll();
        verify(chapterMapper).toResponseDto(chapter);
    }

    @Test
    void getChapterById_ExistingId_ShouldReturnChapterResponseDTO() {
        // Arrange
        Long chapterId = 1L;

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterMapper.toResponseDto(any(Chapter.class))).thenReturn(chapterResponseDTO);

        // Act
        ChapterResponseDTO result = chapterService.getChapterById(chapterId);

        // Assert
        assertNotNull(result);
        assertEquals(chapterResponseDTO, result);

        verify(chapterRepository).findById(chapterId);
        verify(chapterMapper).toResponseDto(chapter);
    }

    @Test
    void getChapterById_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long chapterId = 99L;

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> chapterService.getChapterById(chapterId));

        assertTrue(exception.getMessage().contains("Chapter with id " + chapterId + " not found"));
        verify(chapterRepository).findById(chapterId);
        verify(chapterMapper, never()).toResponseDto(any(Chapter.class));
    }

    @Test
    void getChaptersByCourseId_ExistingCourseId_ShouldReturnListOfChapterResponseDTO() {
        // Arrange
        Long courseId = 1L;
        Chapter chapterWithCourse = Chapter.builder()
                .id(1L)
                .course(Course.builder().id(courseId).build())
                .build();
        List<Chapter> chapters = Collections.singletonList(chapterWithCourse);

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(chapterRepository.findAll()).thenReturn(chapters);
        when(chapterMapper.toResponseDto(any(Chapter.class))).thenReturn(chapterResponseDTO);

        // Act
        List<ChapterResponseDTO> result = chapterService.getChaptersByCourseId(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chapterResponseDTO, result.get(0));

        verify(courseRepository).existsById(courseId);
        verify(chapterRepository).findAll();
        verify(chapterMapper).toResponseDto(any(Chapter.class));
    }

    @Test
    void getChaptersByCourseId_NonExistingCourseId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long courseId = 99L;

        when(courseRepository.existsById(courseId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> chapterService.getChaptersByCourseId(courseId));

        assertTrue(exception.getMessage().contains("Course with id " + courseId + " not found"));
        verify(courseRepository).existsById(courseId);
        verify(chapterRepository, never()).findAll();
    }

    @Test
    void createChapter_ValidInput_ShouldReturnCreatedChapterResponseDTO() {
        // Arrange
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(chapterMapper.toEntity(any(ChapterCreateDTO.class))).thenReturn(chapter);
        when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);
        when(chapterMapper.toResponseDto(any(Chapter.class))).thenReturn(chapterResponseDTO);

        // Act
        ChapterResponseDTO result = chapterService.createChapter(chapterCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(chapterResponseDTO, result);

        verify(courseRepository).findById(courseId);
        verify(chapterMapper).toEntity(chapterCreateDTO);
        verify(chapterRepository).save(any(Chapter.class));
        verify(chapterMapper).toResponseDto(chapter);
    }

    @Test
    void createChapter_NonExistingCourseId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long courseId = 99L;
        chapterCreateDTO.setCourseId(courseId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> chapterService.createChapter(chapterCreateDTO));

        assertTrue(exception.getMessage().contains("Course with id " + courseId + " not found"));
        verify(courseRepository).findById(courseId);
        verify(chapterMapper, never()).toEntity(any(ChapterCreateDTO.class));
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    @Test
    void updateChapter_ExistingId_ShouldReturnUpdatedChapterResponseDTO() {
        // Arrange
        Long chapterId = 1L;

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterRepository.save(any(Chapter.class))).thenReturn(chapter);
        when(chapterMapper.toResponseDto(any(Chapter.class))).thenReturn(chapterResponseDTO);

        // Act
        ChapterResponseDTO result = chapterService.updateChapter(chapterUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(chapterResponseDTO, result);

        verify(chapterRepository).findById(chapterId);
        verify(chapterRepository).save(chapter);
        verify(chapterMapper).toResponseDto(chapter);
    }

    @Test
    void updateChapter_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long chapterId = 99L;
        chapterUpdateDTO.setId(chapterId);

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> chapterService.updateChapter(chapterUpdateDTO));

        assertTrue(exception.getMessage().contains("Chapter with id " + chapterId + " not found"));
        verify(chapterRepository).findById(chapterId);
        verify(chapterRepository, never()).save(any(Chapter.class));
    }

    @Test
    void deleteChapter_ExistingId_ShouldDeleteChapter() {
        // Arrange
        Long chapterId = 1L;

        when(chapterRepository.existsById(chapterId)).thenReturn(true);
        doNothing().when(chapterRepository).deleteById(chapterId);

        // Act
        chapterService.deleteChapter(chapterId);

        // Assert
        verify(chapterRepository).existsById(chapterId);
        verify(chapterRepository).deleteById(chapterId);
    }

    @Test
    void deleteChapter_NonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long chapterId = 99L;

        when(chapterRepository.existsById(chapterId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> chapterService.deleteChapter(chapterId));

        assertTrue(exception.getMessage().contains("Chapter with id " + chapterId + " not found"));
        verify(chapterRepository).existsById(chapterId);
        verify(chapterRepository, never()).deleteById(any());
    }
}