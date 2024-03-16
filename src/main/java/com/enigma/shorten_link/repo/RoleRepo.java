package com.enigma.shorten_link.repo;

import com.enigma.shorten_link.constant.enums.UserRole;
import com.enigma.shorten_link.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface RoleRepo extends JpaRepository<Role, String> {
    @Modifying
    @Query(
        nativeQuery = true,
        value = "SELECT * FROM role WHERE role = :role"
    )
    Optional<Role> findByRole(@Param("role") UserRole role);
    @Modifying
    @Query(
        nativeQuery = true,
        value = "INSERT INTO role (role) VALUES (:role)"
    )
    Role save(@Param("role") Role role);
}