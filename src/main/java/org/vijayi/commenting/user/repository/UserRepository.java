package org.vijayi.commenting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vijayi.commenting.user.repository.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
}
