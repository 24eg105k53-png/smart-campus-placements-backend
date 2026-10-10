package com.smartcampus.placement.util;

import com.smartcampus.placement.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileStorageUtil {

    private final Path storageDirectory;

    public FileStorageUtil(@Value("${app.storage.upload-dir:./uploads/resumes}") String uploadDir) {
        this.storageDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storageDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize upload storage directory", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BadRequestException("Failed to store empty file");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex).toLowerCase();
        }

        if (!extension.equals(".pdf") && !extension.equals(".docx")) {
            throw new BadRequestException("Invalid file format. Only PDF and DOCX files are supported.");
        }

        String safeFileName = UUID.randomUUID() + extension;

        try {
            Path targetLocation = this.storageDirectory.resolve(safeFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return safeFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + safeFileName, ex);
        }
    }

    public Path loadFileAsPath(String fileName) {
        return this.storageDirectory.resolve(fileName).normalize();
    }
}
