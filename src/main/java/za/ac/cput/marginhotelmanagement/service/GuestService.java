package za.ac.cput.marginhotelmanagement.service;
/*
    GuestService.java
    Service implementation for Guest entity
    Author: Hlomla Magopeni (218070349)
    Date: 16 July 2026
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;
import za.ac.cput.marginhotelmanagement.util.Helper;

import java.util.List;

@Service
public class GuestService implements IGuestService {

    private GuestRepository guestRepository;

    @Autowired
    GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest create(Guest guest) {
        validate(guest);
        return guestRepository.save(guest);
    }

    @Override
    public Guest read(Long id) {
        return guestRepository.findById(id).orElse(null);
    }

    @Override
    public Guest update(Guest guest) {
        validate(guest);
        if (guest.getGuestId() == null || !guestRepository.existsById(guest.getGuestId())) {
            return null; // controller turns this into a 404
        }
        return guestRepository.save(guest);
    }

    // Mirrors the validation pattern used in PaymentService: real checks
    // wired into the path a live request actually takes, using the
    // already-existing Helper methods, thrown as plain IllegalArgumentException
    // (caught in GuestController and turned into a 400) rather than a custom
    // exception type.
    private void validate(Guest guest) {
        if (Helper.isNullOrEmpty(guest.getName())) {
            throw new IllegalArgumentException("Guest name is required");
        }
        if (Helper.isNullOrEmpty(guest.getName().getFirstName())
                || Helper.isNullOrEmpty(guest.getName().getLastName())) {
            throw new IllegalArgumentException("Guest first name and last name are required");
        }
        if (Helper.isNullOrEmpty(guest.getContactDetails())) {
            throw new IllegalArgumentException("Guest contact details are required");
        }
        if (Helper.isInvalidEmail(guest.getContactDetails().getEmail())) {
            throw new IllegalArgumentException("A valid email is required");
        }
        if (Helper.isInvalidMobile(guest.getContactDetails().getMobile())) {
            throw new IllegalArgumentException("Mobile number must be exactly 10 digits");
        }
    }

    @Override
    public boolean delete(Guest guest) {
        guestRepository.delete(guest);
        return true;
    }

    @Override
    public List<Guest> findAll() {
        return guestRepository.findAll();
    }

    @Override
    public List<Guest> findByFirstName(String firstName) {
        return guestRepository.findByName_FirstName(firstName);
    }

    @Override
    public List<Guest> findByLastName(String lastName) {
        return guestRepository.findByName_LastName(lastName);
    }

    @Override
    public Guest findByEmail(String email) {
        return guestRepository.findByContactDetails_Email(email);
    }
}