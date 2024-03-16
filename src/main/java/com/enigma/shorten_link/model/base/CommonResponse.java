package com.enigma.shorten_link.model.base;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommonResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private PagingResponse paging;
}
