package za.ac.cput.marginhotelmanagement.dtos;
/*
    Author: Lithabile Lalela (221340963)
    Date :30 August 2026
 */


import lombok.Data;

@Data
public class CreateManagerRequest {
    public String firstName;
    public String middleName;
    public String lastName;
    public String email;
    public String mobile;
    public String officeNumber;


}
