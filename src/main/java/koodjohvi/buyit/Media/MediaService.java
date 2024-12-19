package koodjohvi.buyit.Media;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    @Autowired
    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media saveMedia(Media media) {
        return mediaRepository.save(media);
    }

    public List<Media> getMediaByProductId(String productId) {
        return mediaRepository.findByProductId(productId);
    }

    public void deleteMedia(String mediaId) {
        mediaRepository.deleteById(mediaId);
    }


}
