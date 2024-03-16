package com.enigma.shorten_link.model;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtClaims {
    private String userCredentialId;
    private List<String> roles;
}
