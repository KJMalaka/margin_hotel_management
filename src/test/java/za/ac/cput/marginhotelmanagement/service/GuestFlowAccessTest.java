package za.ac.cput.marginhotelmanagement.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.repository.InvoiceRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GuestFlowAccessTest {
    private final GuestRepository guestRepository = mock(GuestRepository.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final InvoiceRepository invoiceRepository = mock(InvoiceRepository.class);
    private final GuestFlowAccess access = new GuestFlowAccess(
            guestRepository, bookingRepository, invoiceRepository);

    private final Authentication guestPrincipal = new UsernamePasswordAuthenticationToken(
            "guest@example.com",
            "",
            List.of(new SimpleGrantedAuthority("ROLE_USER")));

    @Test
    void guestCanAccessOwnedRecordsOnly() {
        Guest guest = guest("guest@example.com");
        Guest otherGuest = guest("other@example.com");
        Booking booking = new Booking.Builder().setBookingId(15L).setGuest(guest).build();
        Invoice invoice = new Invoice.Builder().setInvoiceId(25L).setBooking(booking).build();

        when(guestRepository.findById(1L)).thenReturn(Optional.of(guest));
        when(guestRepository.findById(2L)).thenReturn(Optional.of(otherGuest));
        when(bookingRepository.findById(15L)).thenReturn(Optional.of(booking));
        when(invoiceRepository.findById(25L)).thenReturn(Optional.of(invoice));

        assertTrue(access.ownsEmail("GUEST@example.com", guestPrincipal));
        assertTrue(access.ownsGuest(1L, guestPrincipal));
        assertTrue(access.ownsBooking(15L, guestPrincipal));
        assertTrue(access.ownsInvoice(25L, guestPrincipal));
        assertFalse(access.ownsEmail("other@example.com", guestPrincipal));
        assertFalse(access.ownsGuest(2L, guestPrincipal));
        assertFalse(access.ownsBooking(99L, guestPrincipal));
        assertFalse(access.ownsInvoice(99L, guestPrincipal));
    }

    @Test
    void nonGuestPrincipalCannotUseGuestOwnershipGrants() {
        Authentication receptionist = new UsernamePasswordAuthenticationToken(
                "staff@example.com",
                "",
                List.of(new SimpleGrantedAuthority("ROLE_RECEPTIONIST")));

        assertFalse(access.ownsEmail("staff@example.com", receptionist));
        assertFalse(access.ownsGuest(1L, receptionist));
        assertFalse(access.ownsBooking(15L, receptionist));
        assertFalse(access.ownsInvoice(25L, receptionist));
    }

    private Guest guest(String email) {
        return new Guest.Builder()
                .setName(new Name.Builder().setFirstName("Guest").setLastName("User").build())
                .setContactDetails(new ContactDetails.Builder().setEmail(email).setMobile("0000000000").build())
                .build();
    }
}
