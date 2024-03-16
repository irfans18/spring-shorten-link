package com.enigma.shorten_link.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for {@link com.enigma.shorten_link.entity.Credential}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}