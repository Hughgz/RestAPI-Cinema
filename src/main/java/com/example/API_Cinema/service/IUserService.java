package com.example.API_Cinema.service;

import com.example.API_Cinema.dto.UserDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.User;

import java.util.List;

public interface IUserService{
    User register(UserDTO dto) throws Exception;
    UserDTO update(UserDTO dto);
    void delete(int id);
    List<UserDTO> getAll();
    List<UserDTO> searchByFullName(String fullName);
    List<UserDTO> searchByPhone(String phone);
    List<UserDTO> searchByEmail(String email);
    UserDTO findById(int id);
    UserDTO convert(User user);

    String login(String phoneNumber, String password, Integer roleId) throws Exception;


}
