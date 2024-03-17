package com.enigma.shorten_link.repository;

import com.enigma.shorten_link.constant.ConstantTable;
import com.enigma.shorten_link.entity.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface LinkRepository extends JpaRepository<Link, String> {

    @Modifying
    @Query(
            nativeQuery = true,
            value = "INSERT INTO " + ConstantTable.LINK + " (id, name, description, short_url, real_url, user_id, created_at) " +
                    "VALUES (:id, :name, :description, :shortUrl, :realUrl, :userId, NOW())"
    )
    void save(
            @Param("id") String id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("shortUrl") String shortUrl,
            @Param("realUrl") String realUrl,
            @Param("userId") String userId
    );

    @Query(
            nativeQuery = true,
            value = "SELECT * " +
                    "FROM " + ConstantTable.LINK + " " +
                    "WHERE short_url = :shortUrl " +
                    "AND deleted_at IS NULL"
    )
    Optional<Link> findByShortUrl(@Param("shortUrl") String shortUrl);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE " + ConstantTable.LINK +
                    " SET name = :name, " +
                    "description = :description, " +
                    "real_url = :realUrl, " +
                    "short_url = :shortUrl, " +
                    "updated_at = NOW() " +
                    "WHERE id = :id"
    )
    void update(
            @Param("id") String id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("realUrl") String realUrl,
            @Param("shortUrl") String shortUrl
    );

    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE " + ConstantTable.LINK + " " +
                    "SET hit_count = hit_count + 1, " +
                    "last_hit_at = NOW() " +
                    "WHERE short_url = :shortUrl"
    )
    void updateHitCount(@Param("shortUrl") String shortUrl);

    @Query(
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) " +
                    "FROM " + ConstantTable.LINK + " " +
                    "WHERE user_id = :userId " +
                    "AND deleted_at IS NULL",
            value = "SELECT * " +
                    "FROM " + ConstantTable.LINK + " " +
                    "WHERE user_id = :userId " +
                    "AND deleted_at IS NULL"
    )
    Page<Link> findAllByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) " +
                    "FROM " + ConstantTable.LINK,
            value = "SELECT * " +
                    "FROM " + ConstantTable.LINK
    )
    Page<Link> findAll(Pageable pageable);

    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE " + ConstantTable.LINK +
                    " SET deleted_at = NOW() WHERE id = :id"
    )
    void softDelete(@Param("id") String id);
}