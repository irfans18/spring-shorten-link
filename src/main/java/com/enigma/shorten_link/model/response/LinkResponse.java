package com.enigma.shorten_link.model.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.enigma.shorten_link.entity.Link}
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkResponse {
    private String id;
    private String name;
    private String description;
    private String shortUrl;
    private String realUrl;
    private Integer hitCount;
    private String userId;
    private Date lastHitAt;
    private Date deleted_at;
    private Date updated_at;
    private Date created_at;
}