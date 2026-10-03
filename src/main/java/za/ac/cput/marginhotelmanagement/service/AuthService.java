package za.ac.cput.marginhotelmanagement.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.domain.dtos.AuthUserResponse;
import za.ac.cput.marginhotelmanagement.domain.dtos.CheckUserEmailResponse;
import za.ac.cput.marginhotelmanagement.domain.dtos.LoginUserRequest;
import za.ac.cput.marginhotelmanagement.domain.dtos.RegisterUserRequest;
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
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            AppUserRepository appUserRepository,
            GuestRepository guestRepository,
            BookingRepository bookingRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ){
        this.appUserRepository = appUserRepository;
        this.guestRepository = guestRepository;
        this.bookingRepository = bookingRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /*Check exisiting email*/
    public CheckUserEmailResponse checkUserEmail(String email) {
        Guest latestGuest = findLatestGuestByEmail(email);
        if(latestGuest == null){
            return new CheckUserEmailResponse(false, null);
        }
        return new CheckUserEmailResponse(true, latestBookingDate(latestGuest));
    }

    /*Register */
    public AuthUserResponse registerUser(RegisterUserRequest registerUserRequest) {
        validate(registerUserRequest);

        if (appUserRepository.findByEmail(registerUserRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        Guest guest = resolveGuest(registerUserRequest);
        String hashedPassword = passwordEncoder.encode(registerUserRequest.getPassword());

        AppUser appUser = new AppUser.Builder()
                .setEmail(registerUserRequest.getEmail())
                .setPassword(hashedPassword)
                .setRole(UserRole.USER) //Always USER on public facing registration
                .setGuest(guest)
                .build();

        AppUser savedAppUser = appUserRepository.save(appUser);
        String token = jwtService.generateToken(savedAppUser.getEmail());
        return new AuthUserResponse(token, savedAppUser.getEmail(), savedAppUser.getRole());
    }

    /*Login App User, AuthController checks for email and password mismatch*/
    public AuthUserResponse loginUser(LoginUserRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        AppUser appUser = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String token = jwtService.generateToken(appUser.getEmail());
        return new AuthUserResponse(token, appUser.getEmail(), appUser.getRole());
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
    private Guest resolveGuest(RegisterUserRequest request) {
        Guest exists = findLatestGuestByEmail(request.getEmail());

        Name name = new Name.Builder()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .build();
        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail(request.getEmail())
                .setMobile(request.getMobile())
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
    private void validate(RegisterUserRequest request){
        if(Helper.isInvalidEmail(request.getEmail())){
            throw new IllegalArgumentException("Invalid email address");
        }
        if (Helper.isNullOrEmpty(request.getFirstName()) || Helper.isNullOrEmpty(request.getLastName())) {
            throw new IllegalArgumentException("First name and last name are required");
        }
        if (Helper.isInvalidMobile(request.getMobile())) {
            throw new IllegalArgumentException("Mobile number must be exactly 10 digits");
        }
        if (Helper.isInvalidPassword(request.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
