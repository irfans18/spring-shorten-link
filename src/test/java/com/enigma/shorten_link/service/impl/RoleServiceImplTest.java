package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.enums.UserRole;
import com.enigma.shorten_link.entity.Role;
import com.enigma.shorten_link.repository.RoleRepository;
import com.enigma.shorten_link.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Slf4j
class RoleServiceImplTest {
    @Mock private RoleRepository repo;
    private RoleService service;


    @BeforeEach
    void setUp() {
        service = new RoleServiceImpl(repo);
    }

    @DisplayName("getOrCreate - Positive Case")
    @Test
    public void getOrCreate_WhenExist_ShouldReturnRole(TestReporter reporter) {
        // Given
        UserRole role = UserRole.ROLE_ADMIN;
        Role existingRole = Role.builder().id("1").role(role).build();

        // Stubbing
        when(repo.findByRole(any())).thenReturn(Optional.of(existingRole));

        // When
        Role result = service.getOrCreate(role);

        // Then
        assertEquals(existingRole, result);
        verify(repo, times(1)).findByRole(any());
        verify(repo, never()).save(any(Role.class));
        reporter.publishEntry("Get or create - Positive Case - done");
    }

    @DisplayName("getOrCreate - Negative Case")
    @Test
    public void getOrCreate_WhenNotExist_ShouldSaveRole(TestReporter reporter) {
        // Given
        UserRole role = UserRole.ROLE_ADMIN;
        Role newRole = Role.builder().role(role).build();

        // Stubbing
        when(repo.findByRole(any())).thenReturn(Optional.empty());
        when(repo.save(any(Role.class))).thenReturn(newRole);

        // When
        Role result = service.getOrCreate(role);

        // Then
        assertEquals(newRole, result);
        verify(repo, times(1)).findByRole(any());
        verify(repo, times(1)).save(any(Role.class));
        reporter.publishEntry("Get or create - Negative Case - done");
    }
}

