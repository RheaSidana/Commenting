package org.vijayi.commenting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vijayi.commenting.user.repository.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
