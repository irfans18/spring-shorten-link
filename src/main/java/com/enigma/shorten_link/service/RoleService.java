package com.enigma.shorten_link.service;

import com.enigma.shorten_link.constant.enums.UserRole;
import com.enigma.shorten_link.entity.Role;

public interface RoleService {
    Role getOrCreate(UserRole role);
}
