package com.electronistore.payload;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private String imageName;
    private String message;
    private Boolean success;
    private HttpStatus status;
}
//* Ye Image response ko proper structure mein return karta hai means JSON format mein.

