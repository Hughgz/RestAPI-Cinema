package com.example.API_Cinema.apis;

import com.example.API_Cinema.dto.UserDTO;
import com.example.API_Cinema.dto.UserLoginDTO;
import com.example.API_Cinema.exception.DataNotFoundException;
import com.example.API_Cinema.model.User;
import com.example.API_Cinema.response.LoginResponse;
import com.example.API_Cinema.response.RegisterResponse;
import com.example.API_Cinema.service.impl.RoleService;
import com.example.API_Cinema.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserApi {
    private final UserService service;
    public UserApi(UserService service ) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ) {
        RegisterResponse registerResponse = new RegisterResponse();

        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            registerResponse.setMessage(errorMessages.toString());
            return ResponseEntity.badRequest().body(registerResponse);
        }
        try {
            User user = service.register(userDTO);
            registerResponse.setMessage("Register successfully");
            registerResponse.setUser(user);
            return ResponseEntity.ok(registerResponse);
        } catch (Exception e) {
            registerResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(registerResponse);
        }
    }
    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable int id, @RequestPart MultipartFile file) throws DataNotFoundException {
        service.uploadImage(id, file);
        return ResponseEntity.ok("Upload successfully");
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO dto, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessage);
            }
            UserDTO userDTO = service.update(dto);
            return ResponseEntity.status(200).body(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userID") int userID) {
        try {
            service.delete(userID);
            return ResponseEntity.ok(String.format("User with id = %d deleted successfully", userID));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> userDTOS = service.getAll();
        return ResponseEntity.status(200).body(userDTOS);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovie(@RequestParam("keyword") String keyword, @RequestParam("title") String title) {
        try {
            if (keyword.equals("fullName")) {
                List<UserDTO> userDTOS = service.searchByFullName(title);
                if (userDTOS.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No User found with the specified full name");
                }
                return ResponseEntity.ok(userDTOS);
            }
            if (keyword.equals("phone")) {
                UserDTO userDTOS = service.searchByPhone(title);
                if (userDTOS == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No User found with the specified phone");
                }
                return ResponseEntity.ok(userDTOS);
            }
            if (keyword.equals("email")) {
                List<UserDTO> userDTOS = service.searchByEmail(title);
                if (userDTOS.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No User found with the specified email");
                }
                return ResponseEntity.ok(userDTOS);
            }
            if (keyword.equals("userID")) {
                try {
                    UserDTO userDTO = service.findById(Integer.parseInt(title));
                    return ResponseEntity.ok(userDTO);
                } catch (RuntimeException e) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with the specified ID");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Please enter the correct search value");
    }

//    @GetMapping("/getRole")
//    public ResponseEntity<?> getRole(@RequestParam("role_id") int roleId){
//        String roleName = roleService.findRoleNameByRoleId(roleId);
//        return ResponseEntity.ok(roleName);
//    }
@PostMapping("/login")
public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
    try {
        // Validate phone number format
        if (!isValidPhoneNumber(userLoginDTO.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message("Số điện thoại không hợp lệ")
                            .build()
            );
        }

        // Attempt login with phone number and password
        String token = service.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
        if (token == null) {
            // If the token is null, it means login failed (incorrect phone number or password)
            return ResponseEntity.badRequest().body(
                    LoginResponse.builder()
                            .message("Số điện thoại hoặc mật khẩu không chính xác")
                            .build()
            );
        }

        // After login success, fetch user details
        UserDTO userDTO = service.searchByPhone(userLoginDTO.getPhoneNumber());
        if (userDTO == null) {
            throw new RuntimeException("Không tìm thấy người dùng, vui lòng thử lại");
        }

        // Create successful login response
        return ResponseEntity.ok(
                LoginResponse.builder()
                        .message("Login successfully")
                        .token(token)
                        .user(userDTO)
                        .build()
        );
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(
                LoginResponse.builder()
                        .message("Tài khoản hoặc mật khẩu không đúng, vui lòng thử lại")
                        .build()
        );
    }
}
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(0[3|5|7|8|9][0-9]{8})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
