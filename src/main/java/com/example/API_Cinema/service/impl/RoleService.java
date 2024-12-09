package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.repository.RoleRepo;
import com.example.API_Cinema.service.RoleInt;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements RoleInt {
    private final RoleRepo repo;

    public RoleService(RoleRepo repo) {
        this.repo = repo;
    }

    @Override
    public String findRoleNameByRoleId(int roleId) {
        return repo.findRoleById(roleId);
    }
}
