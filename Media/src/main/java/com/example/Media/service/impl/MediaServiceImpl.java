package com.example.Media.service.impl;

import com.example.Media.Entity.Media;
import com.example.Media.dtos.MediaDto;
import com.example.Media.exceptions.MediaUploadException;
import com.example.Media.exceptions.ResourceNotFoundException;
import com.example.Media.mapper.MediaMapper;
import com.example.Media.repository.MediaRepository;
import com.example.Media.service.IMediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MediaServiceImpl implements IMediaService {

    private static final String UPLOAD_DIR = "uploads/";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    private static final List<String> ALLOWED_FILE_TYPES = List.of("image/png", "image/jpeg");

    private MediaRepository mediaRepository;

    @Override
    public List<MediaDto> fetchAllMedia() {
        try {
            return mediaRepository.findAll()
                    .stream()
                    .map(media -> MediaMapper.mapToMediaDto(media, new MediaDto()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to fetch media: {}", e.getMessage());
            throw new ResourceNotFoundException("Media", "productId", "Failed to retrieve media list.");
        }
    }

    @Override
    public MediaDto fetchMediaByProductId(String productId) {
        Media media = mediaRepository.findMediaByProductId(productId).orElseThrow(
                () -> new ResourceNotFoundException("Media", "productId", productId)
        );
        return MediaMapper.mapToMediaDto(media, new MediaDto());
    }

    @Override
    public MediaDto uploadMedia(MultipartFile multipartFile, String productId) {
        validateFile(multipartFile);

        try{
            String filename = generateUniqueFileName(multipartFile);
            Path filePath = storeFile(multipartFile, filename);

            Media media = new Media(filePath.toString(), productId);
            mediaRepository.save(media);

            return MediaMapper.mapToMediaDto(media, new MediaDto());

        } catch(IOException e) {
            log.error("File upload failed for product {}: {}", productId, e.getMessage());
            throw new MediaUploadException("File upload failed: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteMedia(String productId) {
        Media media = mediaRepository.findMediaByProductId(productId).orElseThrow(
                () -> new ResourceNotFoundException("Media", "productId", productId)
        );
        try{
            Path filePath = Paths.get(media.getImagePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Failed to delete media file {}: {}", media.getImagePath(), e.getMessage());
        }
        mediaRepository.deleteById(media.getId());
        return true;
    }

    //Validates the file
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new MediaUploadException("File cannot be empty");
        }
        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new MediaUploadException("Invalid file type. Only PNG and JPEG are allowed.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new MediaUploadException("File size exceeds the 2MB limit.");
        }
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename(), "File must have a name")
        );
        String uuid = UUID.randomUUID().toString();
        return uuid + originalFileName;
    }

    private Path storeFile(MultipartFile file, String filename) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        return filePath;
    }
}
