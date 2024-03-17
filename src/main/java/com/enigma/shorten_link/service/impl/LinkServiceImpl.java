package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.ResponseMessage;
import com.enigma.shorten_link.entity.Link;
import com.enigma.shorten_link.model.base.FilterRequest;
import com.enigma.shorten_link.model.request.SearchLinkRequest;
import com.enigma.shorten_link.model.request.ShortenLinkRequest;
import com.enigma.shorten_link.model.response.LinkResponse;
import com.enigma.shorten_link.repository.LinkRepository;
import com.enigma.shorten_link.service.LinkService;
import com.enigma.shorten_link.util.ValidationUtil;
import com.enigma.shorten_link.util.anotation.Alphanumeric;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService {
    private final LinkRepository repo;
    private final ValidationUtil validator;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LinkResponse shorten(ShortenLinkRequest request) {
        validator.validate(request);
        repo.save(
                UUID.randomUUID().toString(),
                request.getName(),
                request.getDescription(),
                request.getShortUrl(),
                request.getRealUrl(),
                request.getUserId()
        );
        return redirect(request.getShortUrl());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LinkResponse update(ShortenLinkRequest request) {
        validator.validate(request);
        repo.update(
                request.getId(),
                request.getName(),
                request.getDescription(),
                request.getRealUrl(),
                request.getShortUrl()
        );
        return redirect(request.getShortUrl());
    }

    @Override
    @Transactional(readOnly = true)
    public LinkResponse redirect(String shortUrl) {
        validator.validate(shortUrl);
        Link link = repo.findByShortUrl(shortUrl).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        return mapToResponse(link);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LinkResponse> findByUserId(SearchLinkRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sortBy = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageRequest = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Page<Link> userPage = repo.findAllByUserId(request.getUserId(), pageRequest);
        return new PageImpl<LinkResponse>(
                userPage.getContent().stream().map(this::mapToResponse).toList(),
                userPage.getPageable(),
                userPage.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LinkResponse> findAll(FilterRequest request){
        if (request.getPage() <= 0) request.setPage(1);
        Sort sortBy = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageRequest = PageRequest.of(request.getPage() - 1, request.getSize(), sortBy);
        Page<Link> userPage = repo.findAll(pageRequest);
        return new PageImpl<LinkResponse>(
                userPage.getContent().stream().map(this::mapToResponse).toList(),
                userPage.getPageable(),
                userPage.getTotalElements()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByid(String id) {
        repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
        repo.softDelete(id);
    }

    private LinkResponse mapToResponse(Link saved) {
        return LinkResponse.builder()
                .id(saved.getId())
                .name(saved.getName() == null ? "N/A" : saved.getName())
                .description(saved.getDescription() == null ? "N/A" : saved.getDescription())
                .shortUrl(saved.getShortUrl())
                .realUrl(saved.getRealUrl())
                .hitCount(saved.getHitCount())
                .userId(saved.getUser() == null ? "N/A" : saved.getUser().getId())
                .lastHitAt(saved.getLastHitAt() == null ? null : saved.getLastHitAt())
                .deleted_at(saved.getDeleted_at() == null ? null : saved.getDeleted_at())
                .updated_at(saved.getUpdated_at() == null ? null : saved.getUpdated_at())
                .created_at(saved.getCreated_at())
                .build();
    }
}
