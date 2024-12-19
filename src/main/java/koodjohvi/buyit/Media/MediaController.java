package koodjohvi.buyit.Media;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<Media> uploadMedia(@RequestBody Media media) {
        return ResponseEntity.ok(mediaService.saveMedia(media));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Media>> getMediaByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(mediaService.getMediaByProductId(productId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable String id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }

}
