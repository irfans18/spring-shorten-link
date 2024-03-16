package com.enigma.shorten_link.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link com.enigma.shorten_link.entity.Link}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortenLinkRequest {
    private String id;
    private String name;
    private String description;
    @NotBlank
    private String shortUrl;
    @NotBlank
    private String realUrl;
    private String userId;
}