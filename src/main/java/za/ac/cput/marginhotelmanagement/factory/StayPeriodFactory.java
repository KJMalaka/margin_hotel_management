package za.ac.cput.marginhotelmanagement.factory;
/*
   Author: Katlego Malaka(230443370)
   Date: 26 June 2026 */

import za.ac.cput.marginhotelmanagement.domain.StayPeriod;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDateTime;

public class StayPeriodFactory {
    public static StayPeriod createStayPeriod(LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        if (Helper.isNullOrEmpty(checkInDate) || Helper.isNullOrEmpty(checkOutDate)) {
            return null;
        }
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            return null;
        }
        return new StayPeriod.Builder()
                .setCheckInDate(checkInDate)
                .setCheckOutDate(checkOutDate)
                .build();
    }
}
