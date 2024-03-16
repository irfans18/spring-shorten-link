package com.enigma.shorten_link.controller;

import com.enigma.shorten_link.constant.APIUrl;
import com.enigma.shorten_link.constant.ResponseMessage;
import com.enigma.shorten_link.model.base.CommonResponse;
import com.enigma.shorten_link.model.base.PagingResponse;
import com.enigma.shorten_link.model.request.SearchLinkRequest;
import com.enigma.shorten_link.model.request.ShortenLinkRequest;
import com.enigma.shorten_link.model.response.LinkResponse;
import com.enigma.shorten_link.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Link")
public class LinkController {
    private final LinkService service;

    @Operation(summary = "Shorten Url")
    @PostMapping(
            value = APIUrl.LINK,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<LinkResponse>> shorten(
            @RequestBody ShortenLinkRequest payload
    ) {
        LinkResponse shorten = service.shorten(payload);
        CommonResponse<LinkResponse> response = CommonResponse.<LinkResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(shorten)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get Link Details By Id")
    @GetMapping(APIUrl.LINK + "/{shortUrl}")
    public ResponseEntity<CommonResponse<LinkResponse>> getUrl(@PathVariable String shortUrl) {
        LinkResponse redirect = service.redirect(shortUrl);

        CommonResponse<LinkResponse> response = CommonResponse.<LinkResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(redirect)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // ResponseEntity<CommonResponse<LinkResponse>>
    @Operation(summary = "Test redirect method")
    @GetMapping(value = "/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse httpServletResponse) {
        LinkResponse redirect = service.redirect(shortUrl);
        httpServletResponse.setHeader("Location", redirect.getRealUrl());
        httpServletResponse.setStatus(HttpStatus.OK.value());
    }

    // @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Update Link")
    @PutMapping(APIUrl.LINK)
    public ResponseEntity<CommonResponse<LinkResponse>> update(
            @RequestBody ShortenLinkRequest payload
    ) {
        LinkResponse update = service.update(payload);
        CommonResponse<LinkResponse> response = CommonResponse.<LinkResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(update)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    // @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Delete Link")
    @DeleteMapping(APIUrl.LINK+"/{id}/delete")
    public ResponseEntity<CommonResponse<String>> delete(@PathVariable String id) {
        service.deleteByid(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }

    // @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Get All Link")
    @GetMapping(APIUrl.LINK)
    public ResponseEntity<CommonResponse<List<LinkResponse>>> findAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "userId", required = false) String userId
    ) {
        SearchLinkRequest request = SearchLinkRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .userId(userId)
                .build();

        Page<LinkResponse> links = service.findAll(request);

        PagingResponse paging = PagingResponse.builder()
                .page(links.getNumber())
                .size(links.getSize())
                .totalElement(links.getTotalElements())
                .totalPages(links.getTotalPages())
                .hasNext(links.hasNext())
                .hasPrevious(links.hasPrevious())
                .build();

        CommonResponse<List<LinkResponse>> response = CommonResponse.<List<LinkResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(links.getContent())
                .paging(paging)
                .build();

        return ResponseEntity
                .ok(response);
    }
}
