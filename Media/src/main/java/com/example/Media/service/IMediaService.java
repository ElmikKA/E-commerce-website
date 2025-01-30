package com.example.Media.service;

import com.example.Media.Entity.Media;
import com.example.Media.dtos.MediaDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IMediaService {

    List<MediaDto> fetchAllMedia();

    MediaDto fetchMediaByProductId(String productId);

    MediaDto uploadMedia(MultipartFile multipartFile, String productId) throws IOException;

    boolean deleteMedia(String mediaId);

}
