package com.example.Media.mapper;

import com.example.Media.Entity.Media;
import com.example.Media.dtos.MediaDto;

public class MediaMapper {

    public static MediaDto mapToMediaDto(Media media, MediaDto mediaDto) {
        mediaDto.setId(media.getId());
        mediaDto.setImagedPath(media.getImagePath());
        mediaDto.setUserId(media.getProductId());
        return mediaDto;
    }

    public static Media mapToMedia(MediaDto mediaDto, Media media) {
        media.setId(mediaDto.getId());
        media.setImagePath(mediaDto.getImagedPath());
        media.setProductId(mediaDto.getUserId());
        return media;
    }
}
