package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.mainservice.dto.course.request.CourseCreateDTO;
import kz.bitlab.mainservice.dto.course.request.CourseUpdateDTO;
import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import kz.bitlab.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Курсы", description = "API для работы с курсами")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Получить все курсы", description = "Возвращает список всех доступных курсов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курсы успешно получены")
    })
    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courseResponseDTOS = courseService.getAllCourses();
        return ResponseEntity.ok(courseResponseDTOS);
    }

    @Operation(summary = "Получить курс по ID", description = "Возвращает курс по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно получен"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        CourseResponseDTO courseResponseDTO = courseService.getCourseById(id);
        return ResponseEntity.ok(courseResponseDTO);
    }

    @Operation(summary = "Создать новый курс", description = "Создает новый курс и возвращает его с присвоенным ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные курса")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseCreateDTO courseCreateDTO) {
        CourseResponseDTO savedCourse = courseService.createCourse(courseCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @Operation(summary = "Обновить курс", description = "Обновляет существующий курс по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Курс не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные курса")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseUpdateDTO courseUpdateDTO) {
        // Установка id из пути в DTO, если он не был установлен
        courseUpdateDTO.setId(id);

        CourseResponseDTO updatedCourse = courseService.updateCourse(courseUpdateDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @Operation(summary = "Удалить курс", description = "Удаляет курс по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Курс успешно удален"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}