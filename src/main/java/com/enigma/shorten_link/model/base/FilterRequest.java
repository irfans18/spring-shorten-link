package com.enigma.shorten_link.model.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FilterRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
}
