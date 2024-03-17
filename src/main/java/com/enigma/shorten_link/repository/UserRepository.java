package com.enigma.shorten_link.repository;

import com.enigma.shorten_link.constant.ConstantTable;
import com.enigma.shorten_link.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(
            nativeQuery = true,
            value = "SELECT *" +
                    "FROM " + ConstantTable.USER +
                    " WHERE id = :id"
    )
    Optional<User> findById(@Param("id") String id);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "INSERT INTO " + ConstantTable.USER +
                    " (id, name, credential_id, created_at) " +
                    "VALUES (:id, :name, :credential_id, NOW())"
    )
    int save(
            @Param("id") String id,
            @Param("name") String name,
            @Param("credential_id") String credential_id
    );
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE " + ConstantTable.USER + " SET " +
                    "name = :name " +
                    "credential_id = :credential_id " +
                    "updated_at = NOW() " +
                    "WHERE id = :id"
    )
    int update(
            @Param("id") String id,
            @Param("name") String name,
            @Param("credential_id") String credential_id
    );
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE " + ConstantTable.USER +
                    " SET deleted_at = NOW() " +
                    "WHERE id = :id"
    )
    int softDelete(@Param("id") String id);
}