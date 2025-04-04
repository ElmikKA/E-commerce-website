package com.example.Media.controller;

import com.example.Media.dtos.MediaDto;
import com.example.Media.service.IMediaService;
import com.sharedDto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping(path = "/api/media", produces = {MediaType.APPLICATION_JSON_VALUE} )
@AllArgsConstructor
public class MediaController {

    private static final Logger log = LoggerFactory.getLogger(MediaController.class);
    private final IMediaService mediaService;
    private static final String UPLOAD_DIR = "uploads";

    @GetMapping()
    public ResponseEntity<List<MediaDto>> getAllMedia(@RequestHeader("buyit-correlation-id") String correlationId) {
        log.debug("buyit-correlation-id found getAllMedia() {}", correlationId);
        return ResponseEntity.ok(mediaService.fetchAllMedia());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<String> getMediaByProductId(
            @RequestHeader("buyit-correlation-id") String correlationId,
            @PathVariable String productId
    ) {
        log.debug("buyit-correlation-id found getMediaProductId() {}", correlationId);
        return ResponseEntity.ok(mediaService.fetchMediaByProductId(productId));
    }


    @GetMapping("/file/{filename}")
    public ResponseEntity<Resource> serveFile(
            @RequestHeader("buyit-correlation-id") String correlationId,
            @PathVariable String filename
    ) {
        log.debug("buyit-correlation-id found serveFile() {}", correlationId);
        Resource resource = mediaService.loadFileAsResource(filename);
        Path filePath = Paths.get("your/upload/directory").resolve(filename).normalize();
        String contentType = mediaService.getContentType(filePath);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping(path = "/delete/{productId}")
    public ResponseEntity<ResponseDto> deleteMedia(
            @RequestHeader("buyit-correlation-id") String correlationId,
            @PathVariable String productId
    ) {
        log.debug("buyit-correlation-id found deleteMedia() {}", correlationId);
        mediaService.deleteMedia(productId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
