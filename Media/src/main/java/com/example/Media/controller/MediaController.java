package com.example.Media.controller;

import com.example.Media.dtos.MediaDto;
import com.example.Media.dtos.ResponseDto;
import com.example.Media.exceptions.MediaUploadException;
import com.example.Media.exceptions.ResourceNotFoundException;
import com.example.Media.service.IMediaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/media", produces = {MediaType.APPLICATION_JSON_VALUE} )
@AllArgsConstructor
public class MediaController {

    private final IMediaService mediaService;

    @GetMapping()
    public ResponseEntity<List<MediaDto>> getAllMedia() {
        return ResponseEntity.ok(mediaService.fetchAllMedia());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<MediaDto> getMediaByProductId(@PathVariable String productId) {
        return ResponseEntity.ok(mediaService.fetchMediaByProductId(productId));
    }

//    @PostMapping(path = "/upload")
//    public ResponseEntity<MediaDto> uploadMedia(@RequestParam("file") MultipartFile file, @RequestParam("productId") String productId) throws IOException {
//        MediaDto mediaDto = mediaService.uploadMedia(file, productId);
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(mediaDto);
//    }

    @DeleteMapping(path = "/delete/{productId}")
    public ResponseEntity<ResponseDto> deleteMedia(@PathVariable String productId) {
        mediaService.deleteMedia(productId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
