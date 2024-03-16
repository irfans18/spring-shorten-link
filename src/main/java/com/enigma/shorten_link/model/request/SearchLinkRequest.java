package com.enigma.shorten_link.model.request;

import com.enigma.shorten_link.model.base.FilterRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SearchLinkRequest extends FilterRequest {
    private String id;
    private String name;
    private String description;
    @NotBlank
    private String shortUrl;
    @NotBlank
    private String realUrl;
    private String userId;
}
