package com.enigma.shorten_link.model.response;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.enigma.shorten_link.entity.User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private String id;
    private Date deleted_at;
    private Date updated_at;
    private Date created_at;
    private String name;
    private String credentialId;
}