package com.enigma.shorten_link.service;

import com.enigma.shorten_link.entity.User;
import com.enigma.shorten_link.model.request.UserUpdateRequest;
import com.enigma.shorten_link.model.response.UserResponse;

public interface UserService {
    User create(User request);
    UserResponse update(UserUpdateRequest request);
    User findOrFail(String id);
    void delete(String id);
}
