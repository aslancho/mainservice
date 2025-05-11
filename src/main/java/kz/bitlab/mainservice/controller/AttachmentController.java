package kz.bitlab.mainservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.mainservice.dto.attachment.request.AttachmentCreateDTO;
import kz.bitlab.mainservice.dto.attachment.response.AttachmentResponseDTO;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Вложения", description = "API для работы с вложениями уроков")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @Operation(summary = "Загрузить вложение", description = "Загружает новое вложение для указанного урока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Вложение успешно загружено"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Урок не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<AttachmentResponseDTO> uploadAttachment(
            @ModelAttribute @Valid AttachmentCreateDTO attachmentCreateDTO) {

        log.info("REST запрос на загрузку вложения для урока ID: {}", attachmentCreateDTO.getLessonId());
        if (attachmentCreateDTO.getFile().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        AttachmentResponseDTO uploadedAttachment = attachmentService.uploadAttachment(attachmentCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedAttachment);
    }

    @Operation(summary = "Скачать вложение", description = "Скачивает вложение по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вложение успешно скачано"),
            @ApiResponse(responseCode = "404", description = "Вложение не найдено"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id) {
        log.info("REST запрос на скачивание вложения ID: {}", id);

        AttachmentResponseDTO attachment = attachmentService.getAttachmentsByLessonId(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Вложение с ID " + id + " не найдено"));

        byte[] fileContent = attachmentService.downloadAttachment(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", attachment.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    @Operation(summary = "Получить вложения урока", description = "Возвращает список всех вложений для указанного урока")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Вложения успешно получены"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByLessonId(@PathVariable Long lessonId) {
        log.info("REST запрос на получение вложений для урока ID: {}", lessonId);
        List<AttachmentResponseDTO> attachments = attachmentService.getAttachmentsByLessonId(lessonId);
        return ResponseEntity.ok(attachments);
    }

    @Operation(summary = "Удалить вложение", description = "Удаляет вложение по указанному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Вложение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Вложение не найдено"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        log.info("REST запрос на удаление вложения ID: {}", id);
        attachmentService.deleteAttachment(id);
        return ResponseEntity.noContent().build();
    }
}