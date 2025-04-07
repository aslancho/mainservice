package kz.bitlab.mainservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.mainservice.dto.course.request.CourseCreateDTO;
import kz.bitlab.mainservice.dto.course.request.CourseUpdateDTO;
import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.service.CourseService;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private ObjectMapper objectMapper;
    private CourseResponseDTO courseResponseDTO;
    private CourseCreateDTO courseCreateDTO;
    private CourseUpdateDTO courseUpdateDTO;

    @BeforeEach
    void setUp() {
        // Инициализация MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(new kz.bitlab.mainservice.exception.GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        // Настройка ObjectMapper для работы с LocalDateTime
        objectMapper.findAndRegisterModules();

        LocalDateTime now = LocalDateTime.now();

        courseResponseDTO = CourseResponseDTO.builder()
                .id(1L)
                .name("Java Developer")
                .description("Полный курс по Java разработке")
                .createdTime(now)
                .updatedTime(now)
                .build();

        courseCreateDTO = CourseCreateDTO.builder()
                .name("Java Developer")
                .description("Полный курс по Java разработке")
                .build();

        courseUpdateDTO = CourseUpdateDTO.builder()
                .id(1L)
                .name("Updated Java Developer")
                .description("Обновленный курс по Java разработке")
                .build();
    }

    @Test
    void getAllCourses() throws Exception {
        List<CourseResponseDTO> courses = Collections.singletonList(courseResponseDTO);

        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Java Developer")))
                .andExpect(jsonPath("$[0].description", is("Полный курс по Java разработке")));
    }

    @Test
    void getCourseById() throws Exception {
        Long courseId = 1L;

        when(courseService.getCourseById(courseId)).thenReturn(courseResponseDTO);

        mockMvc.perform(get("/api/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java Developer")))
                .andExpect(jsonPath("$.description", is("Полный курс по Java разработке")));
    }

    @Test
    void getCourseByIdNotFound() throws Exception {
        Long courseId = 99L;

        when(courseService.getCourseById(courseId)).thenThrow(new ResourceNotFoundException("Course not found"));

        mockMvc.perform(get("/api/courses/{id}", courseId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourse() throws Exception {
        when(courseService.createCourse(any(CourseCreateDTO.class))).thenReturn(courseResponseDTO);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java Developer")))
                .andExpect(jsonPath("$.description", is("Полный курс по Java разработке")));
    }

    @Test
    void updateCourse() throws Exception {
        Long courseId = 1L;

        when(courseService.updateCourse(any(CourseUpdateDTO.class))).thenReturn(courseResponseDTO);

        mockMvc.perform(put("/api/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Java Developer")))
                .andExpect(jsonPath("$.description", is("Полный курс по Java разработке")));
    }

    @Test
    void deleteCourse() throws Exception {
        Long courseId = 1L;

        doNothing().when(courseService).deleteCourse(courseId);

        mockMvc.perform(delete("/api/courses/{id}", courseId))
                .andExpect(status().isNoContent());
    }
}