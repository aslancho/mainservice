package kz.bitlab.mainservice.service.impl;

import kz.bitlab.mainservice.dto.attachment.request.AttachmentCreateDTO;
import kz.bitlab.mainservice.dto.attachment.response.AttachmentResponseDTO;
import kz.bitlab.mainservice.entity.Attachment;
import kz.bitlab.mainservice.entity.Lesson;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.mapper.AttachmentMapper;
import kz.bitlab.mainservice.repository.AttachmentRepository;
import kz.bitlab.mainservice.repository.LessonRepository;
import kz.bitlab.mainservice.service.AttachmentService;
import kz.bitlab.mainservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final LessonRepository lessonRepository;
    private final FileService fileService;
    private final AttachmentMapper attachmentMapper;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public AttachmentResponseDTO uploadAttachment(AttachmentCreateDTO attachmentCreateDTO) {
        log.info("Загрузка вложения для урока с ID: {}", attachmentCreateDTO.getLessonId());

        // Проверяем, существует ли урок
        Lesson lesson = lessonRepository.findById(attachmentCreateDTO.getLessonId())
                .orElseThrow(() -> ResourceNotFoundException.lessonNotFound(attachmentCreateDTO.getLessonId()));

        try {
            // Загружаем файл в MinIO
            String originalFilename = attachmentCreateDTO.getFile().getOriginalFilename();
            String storedFileName = fileService.uploadFile(originalFilename, attachmentCreateDTO.getFile());

            // Создаем запись о вложении в БД
            Attachment attachment = Attachment.builder()
                    .name(originalFilename)
                    .url(storedFileName)
                    .lesson(lesson)
                    .createdTime(LocalDateTime.now())
                    .build();

            Attachment savedAttachment = attachmentRepository.save(attachment);

            AttachmentResponseDTO responseDTO = attachmentMapper.toResponseDto(savedAttachment);
            // Формируем полный URL для скачивания
            responseDTO.setUrl(minioEndpoint + "/" + bucketName + "/" + storedFileName);

            return responseDTO;
        } catch (Exception e) {
            log.error("Ошибка при загрузке вложения", e);
            throw new RuntimeException("Ошибка при загрузке вложения", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadAttachment(Long attachmentId) {
        log.info("Скачивание вложения с ID: {}", attachmentId);

        // Находим вложение в БД
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Вложение с ID " + attachmentId + " не найдено"));

        try (InputStream inputStream = fileService.downloadFile(attachment.getUrl())) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Ошибка при скачивании вложения с ID: {}", attachmentId, e);
            throw new RuntimeException("Ошибка при скачивании вложения", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentResponseDTO> getAttachmentsByLessonId(Long lessonId) {
        log.info("Получение вложений для урока с ID: {}", lessonId);

        // Проверяем, существует ли урок
        if (!lessonRepository.existsById(lessonId)) {
            throw ResourceNotFoundException.lessonNotFound(lessonId);
        }

        List<Attachment> attachments = attachmentRepository.findByLessonId(lessonId);

        return attachments.stream()
                .map(attachment -> {
                    AttachmentResponseDTO dto = attachmentMapper.toResponseDto(attachment);
                    // Формируем полный URL для скачивания
                    dto.setUrl(minioEndpoint + "/" + bucketName + "/" + attachment.getUrl());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAttachment(Long attachmentId) {
        log.info("Удаление вложения с ID: {}", attachmentId);

        // Находим вложение в БД
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Вложение с ID " + attachmentId + " не найдено"));

        // Удаляем файл из MinIO
        fileService.deleteFile(attachment.getUrl());

        // Удаляем запись из БД
        attachmentRepository.delete(attachment);

        log.info("Вложение с ID: {} успешно удалено", attachmentId);
    }
}