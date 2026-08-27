package za.ac.cput.marginhotelmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.enums.BookingChannel;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

        // Find booking by guest ID
        List<Booking> findByGuest_GuestId(Long guestId);

        // Find booking for a specific check-in date
        List<Booking> findByStayPeriodCheckInDate(LocalDateTime stayPeriod_checkInDate);

        // Find booking matching a booking channel(ONLINE, WALK_IN, PHONE)
        List<Booking> findByBookingChannel(BookingChannel bookingChannel);

        // query to find bookings for a room in a date range
        @Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId " +
                        "AND b.stayPeriod.checkInDate < :checkOutDate " +
                        "AND b.stayPeriod.checkOutDate > :checkInDate")
        List<Booking> findOverlappingBookings(
                        @Param("roomId") Long roomId,
                        @Param("checkInDate") LocalDateTime checkInDate,
                        @Param("checkOutDate") LocalDateTime checkOutDate);

        // Count overlapping bookings for an availability check
        @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.roomId = :roomId " +
                        "AND b.stayPeriod.checkInDate < :checkOutDate " +
                        "AND b.stayPeriod.checkOutDate > :checkInDate")
        long countOverlappingBookings(
                        @Param("roomId") Long roomId,
                        @Param("checkInDate") LocalDateTime checkInDate,
                        @Param("checkOutDate") LocalDateTime checkOutDate);

        // Simple finder by room id
        List<Booking> findByRoomId(Long roomId);
}
