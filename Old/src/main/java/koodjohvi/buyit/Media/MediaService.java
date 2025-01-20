package koodjohvi.buyit.Media;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<List<Media>> getMediaByProductId(String productId) {
        List<Media> mediaList = mediaRepository.findByProductId(productId);
        return mediaList.isEmpty() ? Optional.empty() : Optional.of(mediaList);
    }

    public void deleteMedia(String mediaId) {
        mediaRepository.deleteById(mediaId);
    }


}
