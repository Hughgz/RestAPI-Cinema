package com.example.API_Cinema.repo;

import com.example.API_Cinema.model.Branch;
import com.example.API_Cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @Query("SELECT user FROM User user WHERE user.phone LIKE %:phone%")
    Optional<User> findByPhone(@Param("phone") String phone);

    @Query("SELECT user FROM User user WHERE user.fullName LIKE %:fullName%")
    List<User> findByName(@Param("fullName") String fullName);

    @Query("SELECT user FROM User user WHERE user.email LIKE %:email%")
    List<User> findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.phone = :phone")
    boolean existsByPhoneNumber(@Param("phone") String phone);

}