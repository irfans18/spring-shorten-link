package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.ResponseMessage;
import com.enigma.shorten_link.entity.Credential;
import com.enigma.shorten_link.entity.User;
import com.enigma.shorten_link.repository.CredentialRepository;
import com.enigma.shorten_link.service.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CredentialServiceImplTest {

    @Mock
    private CredentialRepository repo;
    private CredentialService service;

    @BeforeEach
    void setUp() {
        service = new CredentialServiceImpl(repo);
    }

    @Test
    void findOrFail_WhenFound_ShouldReturnCredential() {
        // Given
        Credential credential = Credential.builder()
                .id("1")
                .build();

        // Stubbing
        when(repo.findById(any())).thenReturn(Optional.of(credential));

        // When
        Credential result = service.findOrFail(credential.getId());

        // Then
        assertEquals(credential, result);
        verify(repo, times(1)).findById(any());
    }

    @Test
    void findOrFail_WhenNotFound_ShouldThrowResponseStatusException() {
        // Given
        Credential credential = Credential.builder()
                .id("1")
                .build();

        // Stubbing
        when(repo.findById(any())).thenThrow(
                 new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND)
        );

        // When
        assertThrows(
                ResponseStatusException.class,
                () -> service.findOrFail(credential.getId())
        );

        // Then
        verify(repo, times(1)).findById(any());
    }

    @Test
    void getByContext_WhenFound_ShouldReturnCredential() {
        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Credential credential =  Credential.builder().id("user_id").build();
        when(authentication.getPrincipal()).thenReturn(credential.getId());
        when(repo.findById(any())).thenReturn(Optional.of(credential));

        // When
        Credential result = service.getByContext();

        // Then
        assertEquals(credential, result);
        verify(repo, times(1)).findById(any());
    }

    @Test
    void save_WhenValid_ShouldDoNothing(){
        // Given
        Credential credential = Credential.builder()
                .id("3eafddb7-fe85-41fd-81aa-572dd963cca0")
                .created_at(new Date())
                .user(
                        new User()
                )
                .build();

        // Stubbing
       when(repo.saveAndFlush(any())).thenReturn(credential);

        // When
        service.save(credential);

        // Then
        verify(repo, times(1)).saveAndFlush(any());
    }

    @Test
    void loadUserByUsername_WhenFound_ShouldReturnUserDetails() {
        // Given
        Credential credential = Credential.builder()
                .id("1")
                .username("admin")
                .build();

        // Stubbing
        when(repo.findByUsername(any())).thenReturn(Optional.of(credential));

        // When
        UserDetails result = service.loadUserByUsername(credential.getUsername());

        // Then
        assertEquals(credential, result);
        verify(repo, times(1)).findByUsername(any());
    }

    @Test
    void loadUserByUsername_WhenNotFound_ShouldThrowResponseStatusException() {
        // Given
        Credential credential = Credential.builder()
                .id("1")
                .build();

        // Stubbing
        when(repo.findByUsername(any())).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND)
        );

        // When
        assertThrows(
                ResponseStatusException.class,
                () -> service.loadUserByUsername(credential.getId())
        );

        // Then
        verify(repo, times(1)).findByUsername(any());
    }
}