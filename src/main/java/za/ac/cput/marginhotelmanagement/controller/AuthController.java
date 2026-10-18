package za.ac.cput.marginhotelmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.AuthenticationException;
import za.ac.cput.marginhotelmanagement.domain.AppUser;
import za.ac.cput.marginhotelmanagement.domain.dtos.auth.*;
import za.ac.cput.marginhotelmanagement.service.AuthService;
import za.ac.cput.marginhotelmanagement.service.GuestEmailStatus;
import za.ac.cput.marginhotelmanagement.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/check-email")
    public ResponseEntity<CheckUserEmailResponse> checkEmail(@RequestBody CheckUserEmailRequest request) {
        GuestEmailStatus status = authService.checkUserEmail(request.getEmail());
        return ResponseEntity.ok(new CheckUserEmailResponse(status.emailExists(), status.lastVisitDate()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
        try {
            AppUser appUser = authService.registerUser(
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getMobile(),
                    request.getPassword());
            AuthUserResponse response = toAuthUserResponse(appUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest request) {
        try {
            AppUser appUser = authService.loginUser(request.getEmail(), request.getPassword());
            AuthUserResponse response = toAuthUserResponse(appUser);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException | IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }

    private AuthUserResponse toAuthUserResponse(AppUser appUser) {
        String token = jwtService.generateToken(appUser.getEmail());
        return new AuthUserResponse(token, appUser.getEmail(), appUser.getRole());
    }
}
