package za.ac.cput.marginhotelmanagement.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import za.ac.cput.marginhotelmanagement.domain.Booking;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Invoice;
import za.ac.cput.marginhotelmanagement.repository.BookingRepository;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.repository.InvoiceRepository;

import java.util.Locale;

@Component
public class GuestFlowAccess {
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;

    public GuestFlowAccess(
            GuestRepository guestRepository,
            BookingRepository bookingRepository,
            InvoiceRepository invoiceRepository) {
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public boolean isGuest(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_USER".equals(authority.getAuthority()));
    }

    public boolean ownsEmail(String email, Authentication authentication) {
        return isGuest(authentication)
                && email != null
                && authentication.getName() != null
                && email.equalsIgnoreCase(authentication.getName());
    }

    public boolean ownsGuest(Long guestId, Authentication authentication) {
        return isGuest(authentication)
                && guestId != null
                && guestRepository.findById(guestId)
                .map(guest -> owns(guest, authentication))
                .orElse(false);
    }

    public boolean ownsBooking(Long bookingId, Authentication authentication) {
        return isGuest(authentication)
                && bookingId != null
                && bookingRepository.findById(bookingId)
                .map(booking -> booking.getGuest() != null && owns(booking.getGuest(), authentication))
                .orElse(false);
    }

    public boolean ownsInvoice(Long invoiceId, Authentication authentication) {
        return isGuest(authentication)
                && invoiceId != null
                && invoiceRepository.findById(invoiceId)
                .map(Invoice::getBooking)
                .map(booking -> booking.getGuest() != null && owns(booking.getGuest(), authentication))
                .orElse(false);
    }

    private boolean owns(Guest guest, Authentication authentication) {
        return guest.getContactDetails() != null
                && guest.getContactDetails().getEmail() != null
                && authentication.getName() != null
                && guest.getContactDetails().getEmail().toLowerCase(Locale.ROOT)
                .equals(authentication.getName().toLowerCase(Locale.ROOT));
    }
}
