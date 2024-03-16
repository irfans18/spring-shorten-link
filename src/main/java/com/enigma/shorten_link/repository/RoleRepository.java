package com.enigma.shorten_link.repository;

import com.enigma.shorten_link.constant.ConstantTable;
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
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query(
        nativeQuery = true,
        value = "SELECT * " +
                "FROM " + ConstantTable.ROLE + " " +
                "WHERE role = :role"
    )
    Optional<Role> findByRole(@Param("role") String role);
}