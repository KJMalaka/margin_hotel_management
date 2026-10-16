package za.ac.cput.marginhotelmanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
   Flat shape returned to the client on reads.
   Author: Lithabile Lalela (221340963)
   Date: 30 August 2026
   */
@AllArgsConstructor
@Getter
public class ManagerDto {
    private Long staffId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String mobile;
    private String officeNumber;
}