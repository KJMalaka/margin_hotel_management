package za.ac.cput.marginhotelmanagement.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.enums.UserRole;
import za.ac.cput.marginhotelmanagement.repository.*;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            AppUserRepository appUserRepository,
            GuestRepository guestRepository,
            BookingRepository bookingRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ){
        this.appUserRepository = appUserRepository;
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /*Check exisiting email*/
    public GuestEmailStatus checkUserEmail(String email) {
        Guest latestGuest = findLatestGuestByEmail(email);
        if(latestGuest == null){
            return new GuestEmailStatus(false, null);
        }
        return new GuestEmailStatus(true, latestBookingDate(latestGuest));
    }

    /*Register */
    public AppUser registerUser(String email, String firstName, String lastName, String mobile, String password) {
        validate(email, firstName, lastName, mobile, password);

        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        Guest guest = resolveGuest(email, firstName, lastName, mobile);
        String hashedPassword = passwordEncoder.encode(password);

        AppUser appUser = new AppUser.Builder()
                .setEmail(email)
                .setPassword(hashedPassword)
                .setRole(UserRole.USER) //Always USER on public facing registration
                .setGuest(guest)
                .build();

        return appUserRepository.save(appUser);
    }

    /*Login App User, AuthController checks for email and password mismatch*/
    public AppUser loginUser(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    /*Private Helpers*/
    private Guest findLatestGuestByEmail(String email) {
        List<Guest> matches = guestRepository.findByContactDetails_Email(email);
        if(matches.isEmpty()){
            return null;
        }
        return matches.stream()
                .max(Comparator.comparing(this::latestBookingDate,
                        Comparator.nullsFirst(Comparator.naturalOrder())))
                .orElse(matches.get(0));
    }

    private LocalDate latestBookingDate(Guest guest) {
        List<Booking> bookings = bookingRepository.findByGuest_GuestId(guest.getGuestId());
        return bookings.stream()
                .map(b -> b.getStayPeriod() != null ? b.getStayPeriod().getCheckInDate() : null)
                .filter(Objects::nonNull)
                .map(java.time.LocalDateTime::toLocalDate)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    //Recreate Guest information, if guest exists, update the name and contact details, else create a new guest
    private Guest resolveGuest(String email, String firstName, String lastName, String mobile) {
        Guest exists = findLatestGuestByEmail(email);

        Name name = new Name.Builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();
        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(email)
                .setMobile(mobile)
                .build();
        if (exists != null) {
            Guest update = new Guest.Builder()
                    .copy(exists)
                    .setName(name)
                    .setContactDetails(contactDetails)
                    .build();
            return guestRepository.save(update);
        }
        Guest updated = new Guest.Builder()
                .setName(name)
                .setContactDetails(contactDetails)
                .build();
        return guestRepository.save(updated);
    }

    //Validate user input
    private void validate(String email, String firstName, String lastName, String mobile, String password) {
        if(Helper.isInvalidEmail(email)){
            throw new IllegalArgumentException("Invalid email address");
        }
        if (Helper.isNullOrEmpty(firstName) || Helper.isNullOrEmpty(lastName)) {
            throw new IllegalArgumentException("First name and last name are required");
        }
        if (Helper.isInvalidMobile(mobile)) {
            throw new IllegalArgumentException("Mobile number must be exactly 10 digits");
        }
        if (Helper.isInvalidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
