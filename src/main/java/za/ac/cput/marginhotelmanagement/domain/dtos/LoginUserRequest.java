package za.ac.cput.marginhotelmanagement.domain.dtos;

import lombok.Data;

@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
