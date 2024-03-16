package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.enums.UserRole;
import com.enigma.shorten_link.entity.Role;
import com.enigma.shorten_link.repo.RoleRepo;
import com.enigma.shorten_link.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo repo;
    @Override
    public Role getOrCreate(UserRole role) {
        return repo.findByRole(role).orElseGet(() -> repo.save(Role.builder().role(role).build()));
    }
}
