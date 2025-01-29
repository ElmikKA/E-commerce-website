package com.example.Media.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "media")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Media {

    @Id
    private String id;

    private String imagedPath;

    private String userId;

}
