package com.enigma.shorten_link.model.response;

import lombok.*;

import java.util.List;

/**
 * DTO for {@link com.enigma.shorten_link.entity.Credential}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterResponse {
    private String username;
    private List<String> roles;
}