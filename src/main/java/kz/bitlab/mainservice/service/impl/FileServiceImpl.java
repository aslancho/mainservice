package kz.bitlab.mainservice.service.impl;

import io.minio.*;
import kz.bitlab.mainservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(String originalFileName, MultipartFile file) {
        try {
            // Генерируем уникальное имя файла
            String fileExtension = getFileExtension(originalFileName);
            String uniqueFileName = UUID.randomUUID() + fileExtension;

            // Загружаем файл в MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uniqueFileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("Файл успешно загружен в MinIO: {}", uniqueFileName);
            return uniqueFileName;

        } catch (Exception e) {
            log.error("Ошибка при загрузке файла в MinIO", e);
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }

    @Override
    public InputStream downloadFile(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Ошибка при скачивании файла из MinIO: {}", fileName, e);
            throw new RuntimeException("Ошибка при скачивании файла", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            log.info("Файл успешно удален из MinIO: {}", fileName);
        } catch (Exception e) {
            log.error("Ошибка при удалении файла из MinIO: {}", fileName, e);
            throw new RuntimeException("Ошибка при удалении файла", e);
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}