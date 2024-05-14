package com.dmytro.gameboot.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUploadUtil {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    public static String saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        String fileExtension = getFileExtension(fileName);
        if(fileExtension.equals("")){
            fileExtension = "png";
        }
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("Only image files (jpg, jpeg, png) are allowed.");
        }
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try {
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName + "." + fileExtension));
        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
        return fileName + "." + fileExtension;
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}