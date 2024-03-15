package com.enigma.shorten_link.repo;

import com.enigma.shorten_link.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepo extends JpaRepository<Credential, String> {
}