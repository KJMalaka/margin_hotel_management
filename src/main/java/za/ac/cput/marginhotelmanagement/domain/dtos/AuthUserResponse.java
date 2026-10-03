package za.ac.cput.marginhotelmanagement.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import za.ac.cput.marginhotelmanagement.enums.UserRole;

@Getter
@AllArgsConstructor
public class AuthUserResponse {
    private String jwtToken;
    private String email;
    private UserRole role;
}
