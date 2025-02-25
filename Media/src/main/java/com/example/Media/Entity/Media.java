package com.example.Media.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "media")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Media {

    @Id
    private String id;

    private String imagePath;

    private String productId;

    public Media(String imagePath, String productId) {
        this.imagePath = imagePath;
        this.productId = productId;
    }

}
