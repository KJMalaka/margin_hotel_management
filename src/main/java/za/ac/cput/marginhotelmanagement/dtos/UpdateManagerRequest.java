package za.ac.cput.marginhotelmanagement.dtos;

/*
    Author: Lithabile Lalela (221340963)
    Date :30 August 2026
 */

import lombok.Data;

@Data
public class UpdateManagerRequest {
    private Long staffId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
    private String officeNumber;
}