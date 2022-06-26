package com.dlpmeh.config.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dlpmeh.config.model.User;

/**
 * Repository interface for User entity with CRUD operations and custom queries
 * by email and full name.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    public User findByEmail(String email);

    // Search users by full name or email containing the query
    @Query("SELECT DISTINCT u FROM User u WHERE u.fullName LIKE %:query% OR u.email LIKE %:query%")
    public List<User> searchUser(@Param("query") String query);

}
