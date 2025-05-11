package kz.bitlab.mainservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
    String uploadFile(String fileName, MultipartFile file);
    InputStream downloadFile(String fileName);
    void deleteFile(String fileName);
}