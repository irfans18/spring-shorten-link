package com.enigma.shorten_link.repository;

import com.enigma.shorten_link.constant.ConstantTable;
import com.enigma.shorten_link.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface CredentialRepository extends JpaRepository<Credential, String> {
    @Query(
    nativeQuery = true,
    value = "SELECT *" +
            "FROM " + ConstantTable.CREDENTIAL + " " +
            "WHERE id = :id"
    )
    Optional<Credential> findById(@Param("id") String id);

    @Query(
    nativeQuery = true,
    value = "SELECT *" +
            "FROM " + ConstantTable.CREDENTIAL + " " +
            "WHERE username = :username"
    )
    Optional<Credential> findByUsername(@Param("username") String username);
}