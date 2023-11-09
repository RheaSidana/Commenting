package org.vijayi.commenting.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vijayi.commenting.user.repository.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
        value = "Select * from users where name= ?1 and id= ?2", nativeQuery = true
    )
    Optional<User> findByName(String name, Long id);
}
