package za.ac.cput.marginhotelmanagement.controller;
/*
   GuestControllerTest.java
   Author: Hlomla Magopeni (218070349)
   Date: 21 August 2026
   */

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import za.ac.cput.marginhotelmanagement.domain.Guest;
import za.ac.cput.marginhotelmanagement.domain.Name;
import za.ac.cput.marginhotelmanagement.factory.GuestFactory;
import za.ac.cput.marginhotelmanagement.repository.GuestRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GuestControllerTest {

    private static Guest guest;
    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private TestRestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/guest";

    @BeforeEach
    void setUp() {
        Guest newGuest = GuestFactory.createGuest(
                new Name.Builder()
                        .setFirstName("Hlomla")
                        .setMiddleName("M")
                        .setLastName("Magopeni")
                        .build(),
                new ContactDetails.Builder()
                        .setEmail("hlomla.magopeni@example.com")
                        .setMobile("0821234567")
                        .build());
        assertNotNull(newGuest, "Mock guest creation failed");
        GuestControllerTest.guest = newGuest;
    }

    @Test
    @Order(1)
    void create() {
        String url = BASE_URL + "/create";
        ResponseEntity<Guest> response = this.restTemplate.postForEntity(url, guest, Guest.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Guest guestSaved = response.getBody();
        assertNotNull(guestSaved);
        GuestControllerTest.guest = guestSaved;
        System.out.println("Created Guest: " + guestSaved);
    }

    @Test
    @Order(2)
    void read() {
        String url = BASE_URL + "/read/" + guest.getGuestId();
        ResponseEntity<Guest> response = this.restTemplate.getForEntity(url, Guest.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Guest guestRead = response.getBody();
        assertNotNull(guestRead);
        System.out.println("Read Guest: " + guestRead);
    }

    @Test
    @Order(3)
    void update() {
        String url = BASE_URL + "/update";
        Guest updatedGuest = new Guest.Builder()
                .copy(guest)
                .setContactDetails(new ContactDetails.Builder()
                        .setEmail("hlomla.updated@example.com")
                        .setMobile("0839876543")
                        .build())
                .build();
        this.restTemplate.put(url, updatedGuest);
        ResponseEntity<Guest> response = this.restTemplate.getForEntity(BASE_URL + "/read/" + guest.getGuestId(), Guest.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("hlomla.updated@example.com", response.getBody().getContactDetails().getEmail());
        System.out.println("Updated Guest: " + response.getBody());
    }

    @Test
    @Order(4)
    void getAll() {
        String url = BASE_URL + "/getall";
        ResponseEntity<Guest[]> response = this.restTemplate.getForEntity(url, Guest[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Get All Guests");
        if (response.getBody() != null) {
            for (Guest g : response.getBody()) {
                System.out.println(g);
            }
        }
    }

    @Test
    @Order(5)
    void findByFirstName() {
        String url = BASE_URL + "/findByFirstName/Hlomla";
        ResponseEntity<Guest[]> response = this.restTemplate.getForEntity(url, Guest[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Find Guests by First Name");
        if (response.getBody() != null) {
            for (Guest g : response.getBody()) {
                System.out.println(g);
            }
        }
    }

    @Test
    @Order(6)
    void findByLastName() {
        String url = BASE_URL + "/findByLastName/Magopeni";
        ResponseEntity<Guest[]> response = this.restTemplate.getForEntity(url, Guest[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Find Guests by Last Name");
        if (response.getBody() != null) {
            for (Guest g : response.getBody()) {
                System.out.println(g);
            }
        }
    }

    @Test
    @Order(7)
    void findByEmail() {
        String url = BASE_URL + "/findByEmail/hlomla.updated@example.com";
        ResponseEntity<Guest> response = this.restTemplate.getForEntity(url, Guest.class);
        assertNotNull(response);
        System.out.println("Find Guest by Email: " + response.getBody());
    }

    @Test
    @Disabled
    @Order(8)
    void delete() {
        String url = BASE_URL + "/delete/" + guest.getGuestId();
        this.restTemplate.delete(url);
        ResponseEntity<Guest> response = this.restTemplate.getForEntity(BASE_URL + "/read/" + guest.getGuestId(), Guest.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        System.out.println("Deleted Guest");
    }
}
