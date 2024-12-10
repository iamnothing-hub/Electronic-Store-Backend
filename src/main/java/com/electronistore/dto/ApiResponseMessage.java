package com.electronistore.dto;

import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMessage {

    private String message;
    private Boolean success;
    private HttpStatus status;
}
//* Ye API response ko proper structure mein return karta hai means JSON format mein.
