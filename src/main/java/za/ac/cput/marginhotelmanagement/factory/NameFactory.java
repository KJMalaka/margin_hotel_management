package za.ac.cput.marginhotelmanagement.factory;
/*
   NameFactory.java
   Factory for creating Name value objects
   Author: Hlomla Magopeni (218070349)
   Date: 26 June 2026 */

import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.util.Helper;

public class NameFactory {
    public static Name createName(String firstName, String middleName, String lastName) {
        if (Helper.isNullOrEmpty(firstName) ||
                Helper.isNullOrEmpty(lastName)) {
            return null;
        }
        return new Name.Builder()
                .setFirstName(firstName)
                .setMiddleName(middleName)
                .setLastName(lastName)
                .build();
    }
}