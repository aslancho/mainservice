package kz.bitlab.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.mainservice.dto.chapter.request.ChapterCreateDTO;
import kz.bitlab.mainservice.dto.chapter.request.ChapterUpdateDTO;
import kz.bitlab.mainservice.dto.chapter.response.ChapterResponseDTO;
import kz.bitlab.mainservice.exception.GlobalExceptionHandler;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.service.impl.ChapterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ChapterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChapterServiceImpl chapterService;

    @InjectMocks
    private ChapterController chapterController;

    private ObjectMapper objectMapper;
    private ChapterResponseDTO chapterResponseDTO;
    private ChapterCreateDTO chapterCreateDTO;
    private ChapterUpdateDTO chapterUpdateDTO;

    @BeforeEach
    void setUp() {
        // Инициализация MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(chapterController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        LocalDateTime now = LocalDateTime.now();

        chapterResponseDTO = ChapterResponseDTO.builder()
                .id(1L)
                .name("Введение в Java")
                .description("Знакомство с основами Java")
                .order(1)
                .courseId(1L)
                .createdTime(now)
                .updatedTime(now)
                .build();

        chapterCreateDTO = ChapterCreateDTO.builder()
                .name("Введение в Java")
                .description("Знакомство с основами Java")
                .order(1)
                .courseId(1L)
                .build();

        chapterUpdateDTO = ChapterUpdateDTO.builder()
                .id(1L)
                .name("Обновленное введение в Java")
                .description("Обновленное знакомство с Java")
                .order(2)
                .build();
    }

    @Test
    void getAllChapters() throws Exception {
        List<ChapterResponseDTO> chapters = Collections.singletonList(chapterResponseDTO);

        when(chapterService.getAllChapters()).thenReturn(chapters);

        mockMvc.perform(get("/api/chapters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Введение в Java")))
                .andExpect(jsonPath("$[0].description", is("Знакомство с основами Java")))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[0].courseId", is(1)));
    }

    @Test
    void getChapterById() throws Exception {
        Long chapterId = 1L;

        when(chapterService.getChapterById(chapterId)).thenReturn(chapterResponseDTO);

        mockMvc.perform(get("/api/chapters/{id}", chapterId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Введение в Java")))
                .andExpect(jsonPath("$.description", is("Знакомство с основами Java")))
                .andExpect(jsonPath("$.order", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)));
    }

    @Test
    void getChapterById_NotFound() throws Exception {
        Long chapterId = 99L;

        when(chapterService.getChapterById(chapterId)).thenThrow(
                new ResourceNotFoundException("Chapter not found"));

        mockMvc.perform(get("/api/chapters/{id}", chapterId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createChapter() throws Exception {
        when(chapterService.createChapter(any(ChapterCreateDTO.class))).thenReturn(chapterResponseDTO);

        mockMvc.perform(post("/api/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Введение в Java")))
                .andExpect(jsonPath("$.description", is("Знакомство с основами Java")))
                .andExpect(jsonPath("$.order", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)));
    }

    @Test
    void createChapter_InvalidInput() throws Exception {
        ChapterCreateDTO invalidDTO = new ChapterCreateDTO();
        // Пропущены обязательные поля

        mockMvc.perform(post("/api/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateChapter() throws Exception {
        Long chapterId = 1L;

        when(chapterService.updateChapter(any(ChapterUpdateDTO.class))).thenReturn(chapterResponseDTO);

        mockMvc.perform(put("/api/chapters/{id}", chapterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Введение в Java")))
                .andExpect(jsonPath("$.description", is("Знакомство с основами Java")))
                .andExpect(jsonPath("$.order", is(1)))
                .andExpect(jsonPath("$.courseId", is(1)));
    }

    @Test
    void updateChapter_NotFound() throws Exception {
        Long chapterId = 99L;
        chapterUpdateDTO.setId(chapterId);

        when(chapterService.updateChapter(any(ChapterUpdateDTO.class)))
                .thenThrow(new ResourceNotFoundException("Chapter not found"));

        mockMvc.perform(put("/api/chapters/{id}", chapterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterUpdateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteChapter() throws Exception {
        Long chapterId = 1L;

        doNothing().when(chapterService).deleteChapter(chapterId);

        mockMvc.perform(delete("/api/chapters/{id}", chapterId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteChapter_NotFound() throws Exception {
        Long chapterId = 99L;

        doThrow(new ResourceNotFoundException("Chapter not found"))
                .when(chapterService).deleteChapter(chapterId);

        mockMvc.perform(delete("/api/chapters/{id}", chapterId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getChaptersByCourseId() throws Exception {
        Long courseId = 1L;
        List<ChapterResponseDTO> chapters = Collections.singletonList(chapterResponseDTO);

        when(chapterService.getChaptersByCourseId(courseId)).thenReturn(chapters);

        mockMvc.perform(get("/api/chapters/course/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Введение в Java")))
                .andExpect(jsonPath("$[0].courseId", is(1)));
    }

    @Test
    void getChaptersByCourseId_CourseNotFound() throws Exception {
        Long courseId = 99L;

        when(chapterService.getChaptersByCourseId(courseId))
                .thenThrow(new ResourceNotFoundException("Course not found"));

        mockMvc.perform(get("/api/chapters/course/{courseId}", courseId))
                .andExpect(status().isNotFound());
    }
}