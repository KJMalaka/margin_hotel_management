package za.ac.cput.marginhotelmanagement.factory;
/* ContactDetailsFactory.java
   Factory class for ContactDetails value object
   Author: Lithabile Lalela (221340963)
   Date: 28 June 2026 */

import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.util.Helper;

public class ContactDetailsFactory {

    public static ContactDetails createContactDetails(String email, String mobile) {
        if (Helper.isInvalidEmail(email)) return null;
        if (Helper.isInvalidMobile(mobile)) return null;

        return new ContactDetails.Builder()
                .setEmail(email)
                .setMobile(mobile)
                .build();
    }
}