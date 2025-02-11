package com.example.Media.service;

import com.example.Media.Entity.Media;
import com.example.Media.dtos.MediaDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface IMediaService {

    List<MediaDto> fetchAllMedia();

    String fetchMediaByProductId(String productId);

    void uploadMedia(String imageData, String productId);

    boolean deleteMedia(String mediaId);

    Resource loadFileAsResource(String fileName);

    String getContentType(Path filePath);

}
