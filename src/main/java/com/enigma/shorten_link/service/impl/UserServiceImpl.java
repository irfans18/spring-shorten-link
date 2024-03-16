package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.ResponseMessage;
import com.enigma.shorten_link.entity.User;
import com.enigma.shorten_link.model.request.UserUpdateRequest;
import com.enigma.shorten_link.model.response.UserResponse;
import com.enigma.shorten_link.repository.UserRepository;
import com.enigma.shorten_link.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User create(User request) {
        request.setId(UUID.randomUUID().toString());
        repo.save(
                request.getId(),
                request.getName(),
                request.getCredential().getId()
        );
        return findOrFail(request.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse update(UserUpdateRequest request) {
        User user = findOrFail(request.getId());
        user.setName(request.getName());
        user.setUpdated_at(new Date());
        repo.update(user.getId(), user.getName(), user.getCredential().getId());
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User saved) {
        return UserResponse.builder()
                .id(saved.getId())
                .name(saved.getName() == null ? null : saved.getName() )
                .credentialId(saved.getCredential().getId())
                .updated_at(saved.getUpdated_at())
                .deleted_at(saved.getDeleted_at())
                .created_at(saved.getCreated_at())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public User findOrFail(String id) {
        return repo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_USER_NOT_FOUND)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        findOrFail(id);
        repo.softDelete(id);
    }
}
