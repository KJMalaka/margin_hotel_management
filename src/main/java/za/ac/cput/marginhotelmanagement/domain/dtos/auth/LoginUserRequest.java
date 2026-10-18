package za.ac.cput.marginhotelmanagement.domain.dtos.auth;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
