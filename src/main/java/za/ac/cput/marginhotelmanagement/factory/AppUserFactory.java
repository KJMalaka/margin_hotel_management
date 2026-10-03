package za.ac.cput.marginhotelmanagement.factory;
/*
   Author: DM Madondo (230949703)
   Date: 16 September 2026
   */

import za.ac.cput.marginhotelmanagement.domain.AppUser;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.enums.UserRole;
import za.ac.cput.marginhotelmanagement.util.Helper;

import javax.management.relation.Role;

public class AppUserFactory {

    public static AppUser createAppUser(String email, String password, UserRole role, Guest guest) {
        if (Helper.isInvalidEmail(email) || Helper.isNullOrEmpty(password) || Helper.isNullOrEmpty(role)) {
            return null;
        }
        return new AppUser.Builder()
                .setEmail(email)
                .setPassword(password)
                .setRole(role)
                .setGuest(guest)
                .build();
    }
}
