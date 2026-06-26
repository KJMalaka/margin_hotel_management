package za.ac.cput.marginhotelmanagement.factory;

/*
   Author: Katlego Malaka(230443370)
   Date: 26 June 2026 */

import org.junit.jupiter.api.Test;
import za.ac.cput.marginhotelmanagement.domain.StayPeriod;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StayPeriodFactoryTest {


    @Test
    void createStayPeriod() {
        LocalDateTime checkInDate = LocalDateTime.of(2026, 6, 20, 14, 0);
        LocalDateTime checkOutDate = LocalDateTime.of(2026, 6,25,11, 0);

        StayPeriod stayPeriod = StayPeriodFactory.createStayPeriod(checkInDate, checkOutDate);
        assertNotNull(stayPeriod);
        assertEquals(checkInDate, stayPeriod.getCheckInDate());
        assertEquals(checkOutDate, stayPeriod.getCheckOutDate());
        System.out.println("Test Successful" + stayPeriod);
    }
    @Test
    void testCreateStayPeriodWithInvalidDates() {
        LocalDateTime checkInDate = LocalDateTime.of(2026, 6, 20, 14, 0);
        LocalDateTime checkOutDate = LocalDateTime.of(2026, 6, 19, 11, 0);

        StayPeriod stayPeriod = StayPeriodFactory.createStayPeriod(checkInDate, checkOutDate);
        assertNull(stayPeriod);
        System.out.println("Test failed due to invalid dates.");
    }
    @Test
    void testCreateStayPeriodWithNullDates() {
        StayPeriod stayPeriod = StayPeriodFactory.createStayPeriod(null, null);
        assertNull(stayPeriod);
        System.out.println("Test failed due to null dates.");
    }


}