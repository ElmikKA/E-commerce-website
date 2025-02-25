package com.example.Media.controller;

import com.example.Media.dtos.MediaDto;
import com.example.Media.service.IMediaService;
import com.sharedDto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
