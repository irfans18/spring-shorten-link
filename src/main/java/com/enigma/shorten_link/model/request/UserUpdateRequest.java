package com.enigma.shorten_link.model.request;

import lombok.*;

import java.util.Date;

/**
 * DTO for {@link com.enigma.shorten_link.entity.User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserUpdateRequest {
    private String id;
    private Date deleted_at;
    private Date updated_at;
    private Date created_at;
    private String name;
    private String credentialId;
}