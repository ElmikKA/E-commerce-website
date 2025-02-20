package com.example.Media.service;

import com.example.Media.dtos.MediaDto;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.List;

public interface IMediaService {

    List<MediaDto> fetchAllMedia();

    String fetchMediaByProductId(String productId);

    void uploadMedia(String imageData, String productId);

    void deleteMedia(String mediaId);

    Resource loadFileAsResource(String fileName);

    String getContentType(Path filePath);

}
