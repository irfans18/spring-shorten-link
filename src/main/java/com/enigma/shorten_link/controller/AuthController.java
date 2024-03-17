package com.enigma.shorten_link.controller;

import com.enigma.shorten_link.constant.APIUrl;
import com.enigma.shorten_link.constant.ResponseMessage;
import com.enigma.shorten_link.model.base.CommonResponse;
import com.enigma.shorten_link.model.request.AuthRequest;
import com.enigma.shorten_link.model.response.LoginResponse;
import com.enigma.shorten_link.model.response.RegisterResponse;
import com.enigma.shorten_link.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.AUTH)
@Tag(name ="Auth")
public class AuthController {
    private final AuthService service;

    @Operation(summary = "Register User")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody AuthRequest payload) {
        RegisterResponse registerResponse = service.registerUser(payload);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(registerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Register Admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(
            value = "/admin",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<RegisterResponse>> registerAdmin(@RequestBody AuthRequest payload) {
        RegisterResponse registerResponse = service.registerAdmin(payload);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(registerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Login")
    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody AuthRequest request) {

        LoginResponse login = service.login(request);

        CommonResponse<LoginResponse> response = CommonResponse
                .<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_LOGIN)
                .data(login)
                .build();
        return ResponseEntity.ok(response);
    }
}

