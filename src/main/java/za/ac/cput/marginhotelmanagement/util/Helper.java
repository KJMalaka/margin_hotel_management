package za.ac.cput.marginhotelmanagement.util;

import za.ac.cput.marginhotelmanagement.domain.StayPeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Helper {
    // Generates a unique ID using UUID and returns it as a positive long value
    public static Long generateId(){
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    // Returns true if any object passed to it is null
    public static boolean isNullOrEmpty(Object obj) {
        return obj == null;
    }

    // Returns true if the string is null or empty after trimming whitespace
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
   // Returns true if the date is null or in the future
    public static boolean isFutureDate(LocalDate date){
        return date == null || date.isAfter(LocalDate.now());
    }
    // Returns true if the stay period is invalid (null or check-in date is after check-out date)
    public static boolean isInvalidStayPeriod(StayPeriod stayPeriod){
        if (stayPeriod == null || stayPeriod.getCheckInDate() == null || stayPeriod.getCheckOutDate() == null) {
            return true;
        }
        return stayPeriod.getCheckInDate().isAfter(stayPeriod.getCheckOutDate());
    }

    // Payment amount validation: must be > 0
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    // Payment date validation: must not be null or in the future
    public static boolean isValidPaymentDate(LocalDateTime paymentDate) {
        return paymentDate != null && !paymentDate.isAfter(LocalDateTime.now());
    }

}
