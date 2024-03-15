package com.enigma.shorten_link.repo;

import com.enigma.shorten_link.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepo extends JpaRepository<Link, String> {
}