package za.ac.cput.marginhotelmanagement.util;

import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.StayPeriod;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Helper {
    // Generates a unique ID using UUID and returns it as a positive long value
    public static Long generateId() {
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

    // Returns true if the date is null or in the future (uses LocalDateTime)
    public static boolean isFutureDate(LocalDateTime dateTime) {
        return dateTime == null || dateTime.isAfter(LocalDateTime.now());
    }

    // Returns true if the stay period is invalid (null or check-in date is after
    // check-out date)
    public static boolean isInvalidStayPeriod(StayPeriod stayPeriod) {
        if (stayPeriod == null || stayPeriod.getCheckInDate() == null || stayPeriod.getCheckOutDate() == null) {
            return true;
        }
        return stayPeriod.getCheckInDate().isAfter(stayPeriod.getCheckOutDate());
    }

    // Returns true if the room has no overlapping booking for the given dates
    // (LocalDateTime)
    public static boolean isRoomAvailable(List<Booking> existingBookings, LocalDateTime checkInDate,
            LocalDateTime checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("CheckInDate and CheckOutDate must not be null");
        }
        if (!checkInDate.isBefore(checkOutDate)) {
            throw new IllegalArgumentException("CheckInDate must be before CheckOutDate");
        }

        if (existingBookings == null || existingBookings.isEmpty()) {
            return true;
        }

        for (Booking booking : existingBookings) {
            if (booking == null || booking.getStayPeriod() == null) {
                continue;
            }

            LocalDateTime existingCheckIn = booking.getStayPeriod().getCheckInDate();
            LocalDateTime existingCheckOut = booking.getStayPeriod().getCheckOutDate();
            if (existingCheckIn == null || existingCheckOut == null) {
                continue;
            }

            boolean overlaps = existingCheckIn.isBefore(checkOutDate) && existingCheckOut.isAfter(checkInDate);
            if (overlaps) {
                return false;
            }
        }
        return true;
    }

    // Overload: accept LocalDate (user-selected dates) and normalize to
    // LocalDateTime
    public static boolean isRoomAvailable(List<Booking> existingBookings, LocalDate checkInDate,
            LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("CheckInDate and CheckOutDate must not be null");
        }
        if (!checkInDate.isBefore(checkOutDate)) {
            throw new IllegalArgumentException("CheckInDate must be before CheckOutDate");
        }

        LocalDateTime start = checkInDate.atStartOfDay();
        LocalDateTime end = checkOutDate.atTime(23, 59, 59, 999_999_999);
        return isRoomAvailable(existingBookings, start, end);
    }

    // Amount validation: must be > 0 (Invoice & Payment)
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    // Payment date validation: must not be null or in the future
    public static boolean isValidPaymentDate(LocalDateTime paymentDate) {
        return paymentDate != null && !paymentDate.isAfter(LocalDateTime.now());
    }

    // Returns true if the email is null, empty, or missing '@' and '.'
    public static boolean isInvalidEmail(String email) {
        return isNullOrEmpty(email) || !email.contains("@") || !email.contains(".");
    }

    // Returns true if the mobile number is null, empty, or not exactly 10 digits
    public static boolean isInvalidMobile(String mobile) {
        return isNullOrEmpty(mobile) || !mobile.matches("\\d{10}");
    }

}
