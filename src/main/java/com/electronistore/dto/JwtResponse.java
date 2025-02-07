package com.electronistore.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor@NoArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private UserDto userDto;
    private String refreshToken;
}
