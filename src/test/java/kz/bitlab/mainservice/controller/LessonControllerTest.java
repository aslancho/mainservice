package kz.bitlab.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.mainservice.dto.lesson.request.LessonCreateDTO;
import kz.bitlab.mainservice.dto.lesson.request.LessonUpdateDTO;
import kz.bitlab.mainservice.dto.lesson.response.LessonResponseDTO;
import kz.bitlab.mainservice.exception.GlobalExceptionHandler;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.service.impl.LessonServiceImpl;
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
class LessonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LessonServiceImpl lessonService;

    @InjectMocks
    private LessonController lessonController;

    private ObjectMapper objectMapper;
    private LessonResponseDTO lessonResponseDTO;
    private LessonCreateDTO lessonCreateDTO;
    private LessonUpdateDTO lessonUpdateDTO;

    @BeforeEach
    void setUp() {
        // Инициализация MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        LocalDateTime now = LocalDateTime.now();

        lessonResponseDTO = LessonResponseDTO.builder()
                .id(1L)
                .name("Установка JDK")
                .description("Руководство по установке Java Development Kit")
                .content("Подробное руководство по установке JDK")
                .order(1)
                .chapterId(1L)
                .createdTime(now)
                .updatedTime(now)
                .build();

        lessonCreateDTO = LessonCreateDTO.builder()
                .name("Установка JDK")
                .description("Руководство по установке Java Development Kit")
                .content("Подробное руководство по установке JDK")
                .order(1)
                .chapterId(1L)
                .build();

        lessonUpdateDTO = LessonUpdateDTO.builder()
                .id(1L)
                .name("Обновленная установка JDK")
                .description("Обновленное руководство по установке JDK")
                .content("Обновленное подробное руководство по установке JDK")
                .order(2)
                .build();
    }

    @Test
    void getAllLessons() throws Exception {
        List<LessonResponseDTO> lessons = Collections.singletonList(lessonResponseDTO);

        when(lessonService.getAllLessons()).thenReturn(lessons);

        mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Установка JDK")))
                .andExpect(jsonPath("$[0].description", is("Руководство по установке Java Development Kit")))
                .andExpect(jsonPath("$[0].content", is("Подробное руководство по установке JDK")))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[0].chapterId", is(1)));
    }

    @Test
    void getLessonById() throws Exception {
        Long lessonId = 1L;

        when(lessonService.getLessonById(lessonId)).thenReturn(lessonResponseDTO);

        mockMvc.perform(get("/api/lessons/{id}", lessonId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Установка JDK")))
                .andExpect(jsonPath("$.description", is("Руководство по установке Java Development Kit")))
                .andExpect(jsonPath("$.content", is("Подробное руководство по установке JDK")))
                .andExpect(jsonPath("$.order", is(1)))
                .andExpect(jsonPath("$.chapterId", is(1)));
    }

    @Test
    void getLessonById_NotFound() throws Exception {
        Long lessonId = 99L;

        when(lessonService.getLessonById(lessonId)).thenThrow(
                new ResourceNotFoundException("Lesson not found"));

        mockMvc.perform(get("/api/lessons/{id}", lessonId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createLesson() throws Exception {
        when(lessonService.createLesson(any(LessonCreateDTO.class))).thenReturn(lessonResponseDTO);

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Установка JDK")))
                .andExpect(jsonPath("$.description", is("Руководство по установке Java Development Kit")))
                .andExpect(jsonPath("$.content", is("Подробное руководство по установке JDK")))
                .andExpect(jsonPath("$.order", is(1)))
                .andExpect(jsonPath("$.chapterId", is(1)));
    }

    @Test
    void createLesson_InvalidInput() throws Exception {
        LessonCreateDTO invalidDTO = new LessonCreateDTO();
        // Пропущены обязательные поля

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLesson() throws Exception {
        Long lessonId = 1L;

        when(lessonService.updateLesson(any(LessonUpdateDTO.class))).thenReturn(lessonResponseDTO);

        mockMvc.perform(put("/api/lessons/{id}", lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Установка JDK")))
                .andExpect(jsonPath("$.description", is("Руководство по установке Java Development Kit")))
                .andExpect(jsonPath("$.content", is("Подробное руководство по установке JDK")))
                .andExpect(jsonPath("$.order", is(1)))
                .andExpect(jsonPath("$.chapterId", is(1)));
    }

    @Test
    void updateLesson_NotFound() throws Exception {
        Long lessonId = 99L;
        lessonUpdateDTO.setId(lessonId);

        when(lessonService.updateLesson(any(LessonUpdateDTO.class)))
                .thenThrow(new ResourceNotFoundException("Lesson not found"));

        mockMvc.perform(put("/api/lessons/{id}", lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonUpdateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLesson() throws Exception {
        Long lessonId = 1L;

        doNothing().when(lessonService).deleteLesson(lessonId);

        mockMvc.perform(delete("/api/lessons/{id}", lessonId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLesson_NotFound() throws Exception {
        Long lessonId = 99L;

        doThrow(new ResourceNotFoundException("Lesson not found"))
                .when(lessonService).deleteLesson(lessonId);

        mockMvc.perform(delete("/api/lessons/{id}", lessonId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLessonsByChapterId() throws Exception {
        Long chapterId = 1L;
        List<LessonResponseDTO> lessons = Collections.singletonList(lessonResponseDTO);

        when(lessonService.getLessonsByChapterId(chapterId)).thenReturn(lessons);

        mockMvc.perform(get("/api/lessons/chapter/{chapterId}", chapterId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Установка JDK")))
                .andExpect(jsonPath("$[0].chapterId", is(1)));
    }

    @Test
    void getLessonsByChapterId_ChapterNotFound() throws Exception {
        Long chapterId = 99L;

        when(lessonService.getLessonsByChapterId(chapterId))
                .thenThrow(new ResourceNotFoundException("Chapter not found"));

        mockMvc.perform(get("/api/lessons/chapter/{chapterId}", chapterId))
                .andExpect(status().isNotFound());
    }
}