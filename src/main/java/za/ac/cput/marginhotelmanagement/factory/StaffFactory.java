package za.ac.cput.marginhotelmanagement.factory;
/* StaffFactory.java
   Factory class for Staff hierarchy
   Author: Lithabile Lalela (221340963)
   Date: 27 June 2026 */

import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.util.Helper;

public class StaffFactory {

    public static Manager createManager(Long staffId, Name name,
                                        ContactDetails contactDetails,
                                        String officeNumber) {
        if (Helper.isNullOrEmpty(staffId) ||
                Helper.isNullOrEmpty(name) ||
                Helper.isNullOrEmpty(contactDetails) ||
                Helper.isNullOrEmpty(officeNumber)) {
            return null;
        }
        if (staffId <= 0) {
            return null;
        }
        return new Manager.Builder()
                .setStaffId(staffId)
                .setName(name)
                .setContactDetails(contactDetails)
                .setOfficeNumber(officeNumber)
                .build();
    }

    public static Receptionist createReceptionist(Long staffId, Name name,
                                                  ContactDetails contactDetails,
                                                  String deskNumber) {
        if (Helper.isNullOrEmpty(staffId) ||
                Helper.isNullOrEmpty(name) ||
                Helper.isNullOrEmpty(contactDetails) ||
                Helper.isNullOrEmpty(deskNumber)) {
            return null;
        }
        if (staffId <= 0) {
            return null;
        }
        return new Receptionist.Builder()
                .setStaffId(staffId)
                .setName(name)
                .setContactDetails(contactDetails)
                .setDeskNumber(deskNumber)
                .build();
    }
}