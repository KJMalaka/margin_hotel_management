package za.ac.cput.marginhotelmanagement.factory;
/*
   GuestFactory.java
   Factory for creating Guest entities
   Author: Hlomla Magopeni (218070349)
   Date: 26 June 2026 */

import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.util.Helper;

public class GuestFactory {
    public static Guest createGuest(Long guestId, Name name, ContactDetails contactDetails) {
        if (Helper.isNullOrEmpty(guestId) ||
                Helper.isNullOrEmpty(name) ||
                Helper.isNullOrEmpty(contactDetails)) {
            return null;
        }
        if (guestId <= 0) {
            return null;
        }
        return new Guest.Builder()
                .setGuestId(guestId)
                .setName(name)
                .setContactDetails(contactDetails)
                .build();
    }
}