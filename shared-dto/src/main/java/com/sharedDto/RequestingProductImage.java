package com.sharedDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestingProductImage {
    private String productId;
    private String replyTopic;
}
