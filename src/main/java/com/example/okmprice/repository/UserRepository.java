package com.example.okmprice.repository;

import com.example.okmprice.model.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<SiteUser, UUID> {
    public Optional<SiteUser> findByEmail(String email);

}
