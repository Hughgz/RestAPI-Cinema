package com.example.API_Cinema.service.impl;

import com.example.API_Cinema.response.CloudinaryResponse;
import com.example.API_Cinema.utils.FileUploadUtils;
import com.example.API_Cinema.utils.JWTTokenUtils;
import com.example.API_Cinema.dto.UserDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.Role;
import com.example.API_Cinema.model.User;
import com.example.API_Cinema.repository.RoleRepo;
import com.example.API_Cinema.repository.UserRepo;
import com.example.API_Cinema.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final UserRepo repository;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final CloudinaryService cloudinaryService;

    public UserService(UserRepo repository, RoleRepo roleRepo, PasswordEncoder passwordEncoder, JWTTokenUtils jwtTokenUtil, AuthenticationManager authenticationManager, CloudinaryService cloudinaryService) {
        this.repository = repository;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.cloudinaryService = cloudinaryService;
    }


    @Override
    public User register(UserDTO dto) throws Exception{
        String phoneNumber = dto.getPhone();
        // Kiểm tra xem số điện thoại đã tồn tại hay chưa
        if(repository.existsByPhoneNumber(phoneNumber)) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role =roleRepo.findById(dto.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
//        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
//            throw new PermissionDenyException("You cannot register an admin account");
//        }
        User newUser = new ModelMapper().map(dto, User.class);
        newUser.setRole(role);
        String password = dto.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        return repository.save(newUser);
    }
    @Transactional
    public void uploadImage(final Integer id, final MultipartFile file) throws DataNotFoundException {
        User user = this.repository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        FileUploadUtils.assertAllowed(file, FileUploadUtils.IMAGE_PATTERN);
        final String fileName = FileUploadUtils.getFileName(file.getOriginalFilename());
        final CloudinaryResponse response = this.cloudinaryService.uploadFile(file, fileName);
        user.setImage(response.getUrl());
        this.repository.save(user);
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
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(dto.getPassword());
                currentUser.setPassword(encodedPassword);
            }

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
        System.out.println("User List: " + userList); // Log userList
        List<UserDTO> userDTOS = userList.stream().map(this::convert).collect(Collectors.toList());
        System.out.println("User DTOs: " + userDTOS); // Log userDTOS
        return userDTOS;
    }


    @Override
    public List<UserDTO> searchByFullName(String fullName) {
        List<User> userList = repository.findByName(fullName);
        return userList.stream().map(user -> convert(user)).collect(Collectors.toList());
    }

    @Override
    public UserDTO searchByPhone(String phone) {
        User user = repository.findByPhone(phone);
        return convert(user);
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
    public String login(String phoneNumber, String password) throws Exception {
        User existingUser = repository.findByPhone(phoneNumber);
        if (existingUser == null) {
            throw new DataNotFoundException("Phone does not exist");
        }

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Password doesn't match, please try again");
        }

        // Xác thực bằng Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);

        // Tạo JWT token
        return jwtTokenUtil.generateToken(existingUser);
    }

}
