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
        return guestRepository.save(guest);
    }

    @Override
    public Guest read(Long id) {
        return guestRepository.findById(id).orElse(null);
    }

    @Override
    public Guest update(Guest guest) {
        return guestRepository.save(guest);
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