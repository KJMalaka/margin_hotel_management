package za.ac.cput.marginhotelmanagement.service;
/*
    GuestServiceTest.java
    Test for GuestService
    Author: Hlomla Magopeni (218070349)
    Date: 16 July 2026
*/

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.factory.ContactDetailsFactory;
import za.ac.cput.marginhotelmanagement.factory.GuestFactory;
import za.ac.cput.marginhotelmanagement.factory.NameFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GuestServiceTest {

    @Autowired
    private GuestService guestService;

    private Guest mockGuest;
    private Name mockName;
    private ContactDetails mockContactDetails;

    @BeforeAll
    void setUp() {
        mockName = NameFactory.createName("Thandeka", "N", "Zulu");
        assertNotNull(mockName, "Mock name creation failed");

        mockContactDetails = ContactDetailsFactory.createContactDetails(
                "thandeka.zulu@marginhotel.com", "0821234567");
        assertNotNull(mockContactDetails, "Mock contact details creation failed");

        // No id passed in — guestId is @GeneratedValue(IDENTITY), so it must be null before persisting
        mockGuest = GuestFactory.createGuest(mockName, mockContactDetails);
        assertNotNull(mockGuest, "Mock guest creation failed");
    }

    @Test
    @Order(1)
    void create() {
        Guest created = guestService.create(mockGuest);
        assertNotNull(created);
        assertNotNull(created.getGuestId());
        mockGuest = created;
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        Guest read = guestService.read(mockGuest.getGuestId());
        assertNotNull(read);
        assertEquals(mockGuest.getGuestId(), read.getGuestId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void update() {
        Name updatedName = new Name.Builder()
                .copy(mockGuest.getName())
                .setFirstName("Thandi")
                .build();

        Guest updated = new Guest.Builder()
                .copy(mockGuest)
                .setName(updatedName)
                .build();

        Guest result = guestService.update(updated);
        assertNotNull(result);
        assertEquals("Thandi", result.getName().getFirstName());
        mockGuest = result;
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    @Disabled
    void delete() {
        boolean deleted = guestService.delete(mockGuest);
        assertTrue(deleted);
        System.out.println("Deleted guest: " + mockGuest.getGuestId());
    }

    @Test
    @Order(5)
    void findAll() {
        List<Guest> guests = guestService.findAll();
        assertNotNull(guests);
        assertFalse(guests.isEmpty());
        System.out.println("All guests: " + guests);
    }

    @Test
    @Order(6)
    void findByFirstName() {
        List<Guest> guests = guestService.findByFirstName(mockGuest.getName().getFirstName());
        assertNotNull(guests);
        assertFalse(guests.isEmpty());
        System.out.println("Guests by first name: " + guests);
    }

    @Test
    @Order(7)
    void findByLastName() {
        List<Guest> guests = guestService.findByLastName(mockGuest.getName().getLastName());
        assertNotNull(guests);
        assertFalse(guests.isEmpty());
        System.out.println("Guests by last name: " + guests);
    }

    @Test
    @Order(8)
    void findByEmail() {
        Guest guest = guestService.findByEmail(mockGuest.getContactDetails().getEmail());
        assertNotNull(guest);
        assertEquals(mockGuest.getGuestId(), guest.getGuestId());
        System.out.println("Guest by email: " + guest);
    }
}