package com.enigma.shorten_link.repo;

import com.enigma.shorten_link.entity.Link;
import jakarta.persistence.NamedNativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface LinkRepo extends JpaRepository<Link, String> {

    @Modifying
    // @NamedNativeQuery(
    //         name = "LinkRepo.save",
    //         query = "INSERT INTO link (name, description, short_url, real_url, created_at) VALUES (:name, :description, :shortUrl, :realUrl, NOW())",
    //         resultClass = Link.class
    // )
    @Query(
        nativeQuery = true,
        value = "INSERT INTO m_link (id, name, description, short_url, real_url, created_at) " +
                "VALUES (:id, :name, :description, :shortUrl, :realUrl, NOW())"
    )
    void save(
            @Param("id") String id,
            @Param("name") String name,
            @Param("description") String description,
            @Param("shortUrl") String shortUrl,
            @Param("realUrl") String realUrl
    );
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM m_link WHERE short_url = :shortUrl"
    )
    Optional<Link> findByShortUrl(@Param("shortUrl") String shortUrl);
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE m_link SET name = :name, description = :description, real_url = :realUrl, short_url = :shortUrl, updated_at = NOW() WHERE id = :id"
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
            value = "UPDATE m_link SET hit_count = hit_count + 1, last_hit_at = NOW() WHERE short_url = :shortUrl"
    )
    void updateHitCount(@Param("shortUrl") String shortUrl);

    @Query(
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) " +
                    "FROM m_link WHERE user_id = :userId " +
                    "AND WHERE deleted_at IS NULL",
            value = "SELECT * " +
                    "FROM m_link " +
                    "WHERE user_id = :userId " +
                    "AND WHERE deleted_at IS NULL"
    )
    Page<Link> findAllByUserId(@Param("userId") String userId, Pageable pageable);

    @Query(
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) " +
                    "FROM m_link ",
            value = "SELECT * " +
                    "FROM m_link "
    )
    Page<Link> findAll(Pageable pageable);
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE m_link SET deleted_at = NOW() WHERE id = :id"
    )
    void softDelete(@Param("id") String id);
}