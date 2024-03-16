package com.enigma.shorten_link.service;

import com.enigma.shorten_link.model.request.AuthRequest;
import com.enigma.shorten_link.model.response.LoginResponse;
import com.enigma.shorten_link.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerUser(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
