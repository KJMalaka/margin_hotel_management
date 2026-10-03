package za.ac.cput.marginhotelmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;
import za.ac.cput.marginhotelmanagement.domain.dtos.*;
import za.ac.cput.marginhotelmanagement.service.AuthService;
import za.ac.cput.marginhotelmanagement.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/check-email")
    public ResponseEntity<CheckUserEmailResponse> checkEmail(@RequestBody CheckUserEmailRequest request) {
        return ResponseEntity.ok(authService.checkUserEmail(request.getEmail()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
        try {
            AuthUserResponse response = authService.registerUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest request) {
        try {
            AuthUserResponse response = authService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException | IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }
}

