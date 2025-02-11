package com.example.Media.controller;

import com.example.Media.dtos.MediaDto;
import com.example.Media.dtos.ResponseDto;
import com.example.Media.exceptions.MediaUploadException;
import com.example.Media.exceptions.ResourceNotFoundException;
import com.example.Media.service.IMediaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/media", produces = {MediaType.APPLICATION_JSON_VALUE} )
@AllArgsConstructor
public class MediaController {

    private final IMediaService mediaService;

    private static final String UPLOAD_DIR = "uploads";

    @GetMapping()
    public ResponseEntity<List<MediaDto>> getAllMedia() {
        return ResponseEntity.ok(mediaService.fetchAllMedia());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<String> getMediaByProductId(@PathVariable String productId) {
        return ResponseEntity.ok(mediaService.fetchMediaByProductId(productId));
    }

    @GetMapping("/file/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource resource = mediaService.loadFileAsResource(filename);
        Path filePath = Paths.get("your/upload/directory").resolve(filename).normalize();
        String contentType = mediaService.getContentType(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping(path = "/delete/{productId}")
    public ResponseEntity<ResponseDto> deleteMedia(@PathVariable String productId) {
        mediaService.deleteMedia(productId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
