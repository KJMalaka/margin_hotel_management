package za.ac.cput.marginhotelmanagement.factory;
/*
   GuestFactoryTest.java
   Test for GuestFactory
   Author: Hlomla Magopeni (218070349)
   Date: 26 June 2026 */

import org.junit.jupiter.api.*;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GuestFactoryTest {
    private Name name;
    private ContactDetails contactDetails;

    @BeforeEach
    void setUp() {
        this.name = NameFactory.createName("Hlomla", "S", "Magopeni");
        this.contactDetails = new ContactDetails.Builder()
                .setEmail("hlomlam@marginhotel.com")
                .setMobile("0812345678")
                .build();
    }

    @Test
    @Order(1)
    void createGuestSuccess() {
        Guest guest = GuestFactory.createGuest(1L, name, contactDetails);
        assertNotNull(guest);
        assertEquals(1L, guest.getGuestId());
        assertEquals("Hlomla", guest.getName().getFirstName());
        System.out.println("Guest created successfully: " + guest);
    }

    @Test
    @Order(2)
    void createGuestWithInvalidId() {
        Guest guest = GuestFactory.createGuest(-5L, name, contactDetails);
        assertNull(guest);
        System.out.println("Guest creation failed due to invalid ID.");
    }

    @Test
    @Order(3)
    void createGuestWithNullName() {
        Guest guest = GuestFactory.createGuest(2L, null, contactDetails);
        assertNull(guest);
        System.out.println("Guest creation failed due to null name.");
    }

    @Test
    @Order(4)
    void createGuestWithNullContactDetails() {
        Guest guest = GuestFactory.createGuest(3L, name, null);
        assertNull(guest);
        System.out.println("Guest creation failed due to null contact details.");
    }
}