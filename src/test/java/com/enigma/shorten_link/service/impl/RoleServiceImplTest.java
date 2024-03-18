package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.repo.RoleRepo;
import com.enigma.wmb_api.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Slf4j
class RoleServiceImplTest {
    @Mock private RoleRepo repo;
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
        when(repo.findByRole(role)).thenReturn(Optional.of(existingRole));

        // When
        Role result = service.getOrCreate(role);

        // Then
        assertEquals(existingRole, result);
        verify(repo, times(1)).findByRole(role);
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
        when(repo.findByRole(role)).thenReturn(Optional.empty());
        when(repo.save(any(Role.class))).thenReturn(newRole);

        // When
        Role result = service.getOrCreate(role);

        // Then
        assertEquals(newRole, result);
        verify(repo, times(1)).findByRole(role);
        verify(repo, times(1)).save(any(Role.class));
        reporter.publishEntry("Get or create - Negative Case - done");
    }
}

