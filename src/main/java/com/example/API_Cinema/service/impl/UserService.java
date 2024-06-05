package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.components.JWTTokenUtils;
import com.example.API_Cinema.dto.UserDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.exception.PermissionDenyException;
import com.example.API_Cinema.filter.JwtTokenFilter;
import com.example.API_Cinema.model.Role;
import com.example.API_Cinema.model.User;
import com.example.API_Cinema.repo.RoleRepo;
import com.example.API_Cinema.repo.UserRepo;
import com.example.API_Cinema.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepo repository;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JWTTokenUtils jwtTokenUtil;
    @Autowired
    AuthenticationManager authenticationManager;


    @Override
    public User register(UserDTO dto) throws Exception{
        String phoneNumber = dto.getPhone();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if(repository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role =roleRepo.findById(dto.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot register an admin account");
        }
        User newUser = new ModelMapper().map(dto, User.class);
        newUser.setRole(role);
        String password = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return repository.save(newUser);
    }

    @Override
    public UserDTO update(UserDTO dto) {
        User currentUser = repository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("User does not exits"));
        if(currentUser != null){
            currentUser.setFullName(dto.getFullName());
            currentUser.setPhone(dto.getPhone());
            currentUser.setEmail(dto.getEmail());
            currentUser.setBirthdate(dto.getBirthdate());
            currentUser.setSex(dto.getSex());
            currentUser.setArea(dto.getArea());
            String password = dto.getPassword();
            String encoderPass = passwordEncoder.encode(password);
            currentUser.setPassword(encoderPass);

            repository.save(currentUser);
            return convert(currentUser);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserDTO> getAll() {
        List<User> userList = repository.findAll();
        return userList.stream().map(user -> convert(user)).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> searchByFullName(String fullName) {
        List<User> userList = repository.findByName(fullName);
        return userList.stream().map(user -> convert(user)).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> searchByPhone(String phone) {
        Optional<User> userList = repository.findByPhone(phone);
        return userList.stream().map(user -> convert(user)).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> searchByEmail(String email) {
        List<User> userList = repository.findByEmail(email);
        return userList.stream().map(user -> convert(user)).collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(int id) {
        User user = repository.findById(id).orElseThrow(() -> new RuntimeException("User does not exits"));
        return convert(user);
    }

    @Override
    public UserDTO convert(User user) {
        return new ModelMapper().map(user, UserDTO.class);
    }

    @Override
    public String login(String phoneNumber, String password, Integer roleId) throws Exception {
        Optional<User> optionalUser = repository.findByPhone(phoneNumber);
        if(optionalUser.isEmpty()) {
            throw new DataNotFoundException("Phone does not exits");
        }
        //return optionalUser.get();//muốn trả JWT token ?
        User existingUser = optionalUser.get();
        //check password
        if(!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Password doesn't match, please try again");
        }
        Optional<Role> optionalRole = roleRepo.findById(roleId);
        if(optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
            throw new DataNotFoundException("Role not found");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password,
                existingUser.getAuthorities()
        );

        //authenticate with Java Spring security
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
    }
}
