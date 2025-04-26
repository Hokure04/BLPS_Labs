package org.example.blps_lab1.adapters.db.auth;

import org.example.blps_lab1.core.domain.auth.User;
import org.example.blps_lab1.core.ports.db.UserDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
