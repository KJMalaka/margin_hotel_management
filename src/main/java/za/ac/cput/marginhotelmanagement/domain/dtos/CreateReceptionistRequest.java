package za.ac.cput.marginhotelmanagement.dtos;

import lombok.Data;

/*
   Author: Lithabile Lalela (221340963)
   Date: 30 August 2026
   */
@Data
public class CreateReceptionistRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
    private String deskNumber;
}