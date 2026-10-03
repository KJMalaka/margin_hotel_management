package za.ac.cput.marginhotelmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.enums.*;
import za.ac.cput.marginhotelmanagement.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DemoDataSeeder implements CommandLineRunner {
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final ManagerRepository managerRepository;
    private final ReceptionistRepository receptionistRepository;
    private final AppUserRepository appUserRepository;
    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean enabled;

    public DemoDataSeeder(RoomRepository roomRepository,
                          GuestRepository guestRepository,
                          ManagerRepository managerRepository,
                          ReceptionistRepository receptionistRepository,
                          AppUserRepository appUserRepository,
                          BookingRepository bookingRepository,
                          InvoiceRepository invoiceRepository,
                          PaymentRepository paymentRepository,
                          PasswordEncoder passwordEncoder,
                          @Value("${app.demo-data.enabled:true}") boolean enabled) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.managerRepository = managerRepository;
        this.receptionistRepository = receptionistRepository;
        this.appUserRepository = appUserRepository;
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
        this.enabled = enabled;
    }

    @Override
    public void run(String... args) {
        System.out.println("Seeding demo data...");
        if (!enabled) {
            return;
        }
        LocalDate today = LocalDate.now();

        // Rooms
        Room r101 = room(101, RoomType.SINGLE, 650.00, RoomStatus.AVAILABLE);
        Room r102 = room(102, RoomType.SINGLE, 800.00, RoomStatus.OCCUPIED);
        Room r103 = room(103, RoomType.SINGLE, 850.00, RoomStatus.AVAILABLE);
        Room r201 = room(201, RoomType.DOUBLE, 1200.00, RoomStatus.AVAILABLE);
        Room r202 = room(202, RoomType.DOUBLE, 1200.00, RoomStatus.OCCUPIED);
        Room r203 = room(203, RoomType.DOUBLE, 1350.00, RoomStatus.MAINTENANCE);
        Room r301 = room(301, RoomType.SUITE, 2400.00, RoomStatus.AVAILABLE);
        Room r302 = room(302, RoomType.SUITE, 2700.00, RoomStatus.AVAILABLE);

        // Guests
        Guest thabo = guest("Thabo", "Nkosi", "tnkosi@gmail.com", "0821234567");
        Guest lindiwe = guest("Lindiwe", "Dube", "lindiwe43@gmail.com", "0839876543");
        Guest sipho = guest("Sipho", "Mokoena", "siphomokoena@gmail.com", "0711234567");
        Guest ayesha = guest("Ayesha", "Patel", "patelaye@gmail.com", "0728765432");
        Guest james = guest("James", "Okafor", "jamesokafor@gmail.com", "0764561230");

        // Staff
        manager("Nomsa", "Khumalo", "nomsa.khumalo@marginhotel.co.za", "0823456789", "OFF-101");
        receptionist("Zanele", "Mthembu", "zanele.mthembu@marginhotel.co.za", "0834567890", "DESK-01");

        // Logins (the admin login is created by AdminSeeder)
        appUser("receptionist@marginhotel.com", "receptionist123", UserRole.RECEPTIONIST, null);
        appUser("siphomokoena@gmail.com", "guest123", UserRole.USER, sipho);

        // Bookings, invoices and payments (offsets are days from today)
        // Three completed stays, all paid
        Booking b1 = booking(sipho, r201, -30, -27, BookingChannel.ONLINE);
        Invoice i1 = invoice("DEMO-INV-001", 4050.00, InvoiceStatus.PAID, today.minusDays(22), b1);
        payment(i1, 4050.00, PaymentStatus.SUCCESS, today.minusDays(19).atTime(12, 0));

        Booking b2 = booking(thabo, r101, -20, -18, BookingChannel.WALK_IN);
        Invoice i2 = invoice("DEMO-INV-002", 1700.00, InvoiceStatus.PAID, today.minusDays(20), b2);
        payment(i2, 1700.00, PaymentStatus.SUCCESS, today.minusDays(18).atTime(12, 0));

        Booking b3 = booking(ayesha, r301, -10, -7, BookingChannel.TELEPHONIC);
        Invoice i3 = invoice("DEMO-INV-003", 7200.00, InvoiceStatus.PAID, today.minusDays(10), b3);
        payment(i3, 7200.00, PaymentStatus.SUCCESS, today.minusDays(7).atTime(12, 0));

        // Two current stays: one with a failed payment, one not yet paid
        Booking b4 = booking(james, r102, -1, 2, BookingChannel.ONLINE);
        Invoice i4 = invoice("DEMO-INV-004", 2550.00, InvoiceStatus.PENDING, today.minusDays(1), b4);
        payment(i4, 2550.00, PaymentStatus.FAILED, today.minusDays(1).atTime(15, 30));

        Booking b5 = booking(lindiwe, r202, -2, 1, BookingChannel.TELEPHONIC);
        invoice("DEMO-INV-005", 4050.00, InvoiceStatus.PENDING, today.minusDays(2), b5);

        // One upcoming stay, no invoice yet
        booking(thabo, r302, 7, 10, BookingChannel.ONLINE);

        System.out.println("Demo data check complete.");
    }

    // Rooms are matched by room number. Scans the list rather than using
    // findByRoomNumber so a teammate's duplicate room numbers can't throw.
    private Room room(int number, RoomType type, double price, RoomStatus status) {
        return roomRepository.findAll().stream()
                .filter(r -> r.getRoomNumber() == number)
                .findFirst()
                .orElseGet(() -> roomRepository.save(new Room.Builder()
                        .setRoomNumber(number)
                        .setRoomType(type)
                        .setPricePerNight(price)
                        .setRoomStatus(status)
                        .build()));
    }

    private Guest guest(String firstName, String lastName, String email, String mobile) {
        List<Guest> existing = guestRepository.findByContactDetails_Email(email);
        if (!existing.isEmpty()) {
            return existing.get(0);
        }
        Name name = new Name.Builder().setFirstName(firstName).setLastName(lastName).build();
        ContactDetails contact = new ContactDetails.Builder().setEmail(email).setMobile(mobile).build();
        return guestRepository.save(new Guest.Builder().setName(name).setContactDetails(contact).build());
    }

    private void manager(String firstName, String lastName, String email, String mobile, String office) {
        if (managerRepository.existsByContactDetails_Email(email)) {
            return;
        }
        Name name = new Name.Builder().setFirstName(firstName).setLastName(lastName).build();
        ContactDetails contact = new ContactDetails.Builder().setEmail(email).setMobile(mobile).build();
        managerRepository.save(new Manager.Builder()
                .setName(name).setContactDetails(contact).setOfficeNumber(office).build());
    }

    private void receptionist(String firstName, String lastName, String email, String mobile, String desk) {
        if (receptionistRepository.existsByContactDetails_Email(email)) {
            return;
        }
        Name name = new Name.Builder().setFirstName(firstName).setLastName(lastName).build();
        ContactDetails contact = new ContactDetails.Builder().setEmail(email).setMobile(mobile).build();
        receptionistRepository.save(new Receptionist.Builder()
                .setName(name).setContactDetails(contact).setDeskNumber(desk).build());
    }

    private void appUser(String email, String rawPassword, UserRole role, Guest guest) {
        if (appUserRepository.findByEmail(email).isPresent()) {
            return;
        }
        appUserRepository.save(new AppUser.Builder()
                .setEmail(email)
                .setPassword(passwordEncoder.encode(rawPassword))
                .setRole(role)
                .setGuest(guest)
                .build());
    }

    // Returns null if the room is already taken for those dates, so the seeder never double books.
    private Booking booking(Guest guest, Room room, int startOffset, int endOffset, BookingChannel channel) {
        LocalDateTime checkIn = LocalDate.now().plusDays(startOffset).atTime(14, 0);
        LocalDateTime checkOut = LocalDate.now().plusDays(endOffset).atTime(10, 0);

        // This guest already has a booking in this room: reuse it
        for (Booking exists : bookingRepository.findByGuest_GuestId(guest.getGuestId())) {
            if (exists.getRoom() != null && exists.getRoom().getRoomId().equals(room.getRoomId())) {
                return exists;
            }
        }
        if (!bookingRepository.findOverlappingBookings(room.getRoomId(), checkIn, checkOut).isEmpty()) {
            return null;
        }

        StayPeriod stay = new StayPeriod.Builder().setCheckInDate(checkIn).setCheckOutDate(checkOut).build();
        return bookingRepository.save(new Booking.Builder()
                .setBookingDate(LocalDate.now().plusDays(startOffset).minusDays(7))
                .setStayPeriod(stay)
                .setBookingChannel(channel)
                .setGuest(guest)
                .setRoom(room)
                .build());
    }

    private Invoice invoice(String reference, double total, InvoiceStatus status, LocalDate issueDate, Booking booking) {
        if (booking == null) {
            return null;
        }
        return invoiceRepository.findByReference(reference).orElseGet(() ->
                invoiceRepository.save(new Invoice.Builder()
                        .setReference(reference)
                        .setTotalAmount(total)
                        .setStatus(status)
                        .setIssueDate(issueDate)
                        .setBooking(booking)
                        .build()));
    }

    private void payment(Invoice invoice, double amount, PaymentStatus status, LocalDateTime paidAt) {
        if (invoice == null || paymentRepository.existsByInvoice_InvoiceId(invoice.getInvoiceId())) {
            return;
        }
        paymentRepository.save(new Payment.Builder()
                .setAmount(amount)
                .setPaymentStatus(status)
                .setPaymentDate(paidAt)
                .setInvoice(invoice)
                .build());
    }
}
