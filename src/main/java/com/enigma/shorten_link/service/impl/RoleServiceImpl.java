package com.enigma.shorten_link.service.impl;

import com.enigma.shorten_link.constant.enums.UserRole;
import com.enigma.shorten_link.entity.Role;
import com.enigma.shorten_link.repository.RoleRepository;
import com.enigma.shorten_link.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repo;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role getOrCreate(UserRole role) {
        return repo.findByRole(role.name()).orElseGet(() -> repo.save(Role.builder().role(role).created_at(new Date()).build()));
    }
}
