package za.ac.cput.marginhotelmanagement.mappers;

/*
   Converts between Manager/Receptionist entities and their DTOs.
   Plain static methods, same style as the existing Factory classes,
   since Name/ContactDetails use nested Builders that MapStruct
   doesn't map cleanly by default.
   Author: Lithabile Lalela (221340963)
   Date: 30 August 2026
   */

import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Manager;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.domain.Receptionist;
import za.ac.cput.marginhotelmanagement.dtos.*;

public class StaffMapper {

    //Manager

    public static Manager toEntity(CreateManagerRequest request) {
        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setMiddleName(request.getMiddleName())
                .setLastName(request.getLastName())
                .build();

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(request.getEmail())
                .setMobile(request.getMobile())
                .build();

        return new Manager.Builder()
                .setName(name)
                .setContactDetails(contactDetails)
                .setOfficeNumber(request.getOfficeNumber())
                .build();
    }

    public static Manager toEntity(UpdateManagerRequest request) {
        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setMiddleName(request.getMiddleName())
                .setLastName(request.getLastName())
                .build();

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(request.getEmail())
                .setMobile(request.getMobile())
                .build();

        return new Manager.Builder()
                .setStaffId(request.getStaffId())
                .setName(name)
                .setContactDetails(contactDetails)
                .setOfficeNumber(request.getOfficeNumber())
                .build();
    }

    public static ManagerDto toDto(Manager manager) {
        return new ManagerDto(
                manager.getStaffId(),
                manager.getName().getFirstName(),
                manager.getName().getMiddleName(),
                manager.getName().getLastName(),
                manager.getContactDetails().getEmail(),
                manager.getContactDetails().getMobile(),
                manager.getOfficeNumber()
        );
    }

    //Receptionist

    public static Receptionist toEntity(CreateReceptionistRequest request) {
        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setMiddleName(request.getMiddleName())
                .setLastName(request.getLastName())
                .build();

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(request.getEmail())
                .setMobile(request.getMobile())
                .build();

        return new Receptionist.Builder()
                .setName(name)
                .setContactDetails(contactDetails)
                .setDeskNumber(request.getDeskNumber())
                .build();
    }

    public static Receptionist toEntity(UpdateReceptionistRequest request) {
        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setMiddleName(request.getMiddleName())
                .setLastName(request.getLastName())
                .build();

        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(request.getEmail())
                .setMobile(request.getMobile())
                .build();

        return new Receptionist.Builder()
                .setStaffId(request.getStaffId())
                .setName(name)
                .setContactDetails(contactDetails)
                .setDeskNumber(request.getDeskNumber())
                .build();
    }

    public static ReceptionistDto toDto(Receptionist receptionist) {
        return new ReceptionistDto(
                receptionist.getStaffId(),
                receptionist.getName().getFirstName(),
                receptionist.getName().getMiddleName(),
                receptionist.getName().getLastName(),
                receptionist.getContactDetails().getEmail(),
                receptionist.getContactDetails().getMobile(),
                receptionist.getDeskNumber()
        );
    }
}