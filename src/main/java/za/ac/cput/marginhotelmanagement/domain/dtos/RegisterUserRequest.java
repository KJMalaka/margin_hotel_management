package za.ac.cput.marginhotelmanagement.domain.dtos;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String mobile;
    private String password;
}
