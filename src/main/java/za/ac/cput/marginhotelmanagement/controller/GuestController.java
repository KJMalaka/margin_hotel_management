package za.ac.cput.marginhotelmanagement.controller;
/*
   GuestController.java
   REST controller for Guest entity
   Author: Hlomla Magopeni (218070349)
   Date: 21 August 2026
   */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.service.GuestService;

import java.util.List;

@RestController
@RequestMapping("/guest")
public class GuestController {

    private final GuestService guestService;

    @Autowired
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping("/create")
    public ResponseEntity<Guest> create(@RequestBody Guest guest) {
        Guest createdGuest = guestService.create(guest);
        if (createdGuest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdGuest, HttpStatus.CREATED);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Guest> read(@PathVariable Long id) {
        Guest guest = guestService.read(id);
        if (guest != null) {
            return new ResponseEntity<>(guest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Guest> update(@RequestBody Guest guest) {
        Guest updated = guestService.update(guest);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Guest guest = guestService.read(id);
        if (guest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean deleted = guestService.delete(guest);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Guest>> getAll() {
        List<Guest> guests = guestService.findAll();
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/findByFirstName/{firstName}")
    public ResponseEntity<List<Guest>> findByFirstName(@PathVariable String firstName) {
        List<Guest> guests = guestService.findByFirstName(firstName);
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/findByLastName/{lastName}")
    public ResponseEntity<List<Guest>> findByLastName(@PathVariable String lastName) {
        List<Guest> guests = guestService.findByLastName(lastName);
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<Guest> findByEmail(@PathVariable String email) {
        Guest guest = guestService.findByEmail(email);
        if (guest != null) {
            return new ResponseEntity<>(guest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
