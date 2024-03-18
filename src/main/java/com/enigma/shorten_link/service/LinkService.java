package com.enigma.shorten_link.service;

import com.enigma.shorten_link.model.base.FilterRequest;
import com.enigma.shorten_link.model.request.SearchLinkRequest;
import com.enigma.shorten_link.model.request.ShortenLinkRequest;
import com.enigma.shorten_link.model.response.LinkResponse;
import org.springframework.data.domain.Page;

public interface LinkService {

    LinkResponse shorten(ShortenLinkRequest request);
    LinkResponse update(ShortenLinkRequest request);
    LinkResponse redirect(String shortUrl);
    Page<LinkResponse> findByUserId(SearchLinkRequest request);
    Page<LinkResponse> findAll(FilterRequest request);
    void deleteByid(String id);

    void checkUnHitLinkAndInvoke();
}
