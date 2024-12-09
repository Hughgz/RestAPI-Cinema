package com.example.API_Cinema.repository;

import com.example.API_Cinema.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
    @Query("SELECT r.name FROM Role r WHERE r.id = :id")
    String findRoleById(@Param("id") int id);
}
