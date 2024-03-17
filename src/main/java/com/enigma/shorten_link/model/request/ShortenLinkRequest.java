package com.enigma.shorten_link.model.request;

import com.enigma.shorten_link.util.anotation.Alphanumeric;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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
    @Alphanumeric
    private String shortUrl;
    @NotBlank
    private String realUrl;
    private String userId;
}