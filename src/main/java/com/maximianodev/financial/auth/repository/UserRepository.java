package com.maximianodev.financial.auth.repository;

import com.maximianodev.financial.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  boolean existsByEmail(String email);
}
