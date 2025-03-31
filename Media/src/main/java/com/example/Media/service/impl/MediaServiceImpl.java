package com.example.Media.service.impl;

import com.example.Media.Entity.Media;
import com.example.Media.constants.MediaConstants;
import com.example.Media.dtos.MediaDto;
import com.example.Media.exceptions.MediaUploadException;
import com.example.Media.exceptions.ResourceNotFoundException;
import com.example.Media.mapper.MediaMapper;
import com.example.Media.repository.MediaRepository;
import com.example.Media.service.IMediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MediaServiceImpl implements IMediaService {
    private MediaRepository mediaRepository;

    @Override
    public List<MediaDto> fetchAllMedia() {
        try {
            log.info("Fetching all media");
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
    public String fetchMediaByProductId(String productId) {
        Media media = mediaRepository.findMediaByProductId(productId).orElseThrow(
                () -> new ResourceNotFoundException("Media", "productId", productId)
        );
        String filename = Paths.get(media.getImagePath()).getFileName().toString();
        return "http://localhost:9000/api/media/file/" + filename;
    }

    @Override
    public void uploadMedia(String imageData, String productId) {
        if (imageData == null || imageData.isEmpty()) {
            throw new MediaUploadException("Image data is empty");
        }
        try{
            byte[] imageBytes = Base64.getDecoder().decode(imageData);
            String filename = generateUniqueFileName(imageBytes);
            Path filePath = storeFile(imageBytes, filename);

            Media media = new Media(filePath.toString(), productId);
            mediaRepository.save(media);
            log.info("Media uploaded successfully for product {}", productId);

        } catch(IOException e) {
            log.error("File upload failed for product {}: {}", productId, e.getMessage());
            throw new MediaUploadException("File upload failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteMedia(String productId) {
        Media media = mediaRepository.findMediaByProductId(productId).orElseThrow(
                () -> new ResourceNotFoundException("Media", "productId", productId)
        );
        log.info("Deleting media with productId: {}", productId);
        try{
            Path filePath = Paths.get(media.getImagePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.warn("Failed to delete media file {}: {}", media.getImagePath(), e.getMessage());
        }
        mediaRepository.deleteById(media.getId());
        log.info("Media Deleted with productId: {}", productId);
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        String cleanFileName = StringUtils.cleanPath(fileName);
        Path filePath = Paths.get(MediaConstants.UPLOAD_DIR).resolve(cleanFileName).normalize();
        try{
            Resource resource = new UrlResource(filePath.toUri());
            if(!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File", "filename", fileName);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("File", "filename", fileName);
        }
    }

    @Override
    public String getContentType(Path filePath) {
        try {
            String contentType = Files.probeContentType(filePath);
            return (contentType != null) ? contentType : "application/octet-stream";
        } catch (IOException e) {
            throw new RuntimeException("Could not determine file type for " + filePath);
        }
    }

    private String generateUniqueFileName(byte[] file) {
        String uuid = UUID.randomUUID().toString();
        return uuid + ".jpg";
    }

    private Path storeFile(byte[] fileData, String filename) throws IOException {
        Path uploadPath = Paths.get(MediaConstants.UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, fileData);
        return filePath;
    }
}
