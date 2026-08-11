package za.ac.cput.marginhotelmanagement.controller;
/*
   Author: Lithabile Lalela (221340963)
   Date: 19 July 2026 */

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import za.ac.cput.marginhotelmanagement.domain.*;
import za.ac.cput.marginhotelmanagement.factory.StaffFactory;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StaffControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static Manager manager;
    private static Receptionist receptionist;
    private static Long managerId;
    private static Long receptionistId;

    private String MANAGER_URL() {
        return "http://localhost:" + port + "/marginhotel/staff/manager";
    }

    private String RECEPTIONIST_URL() {
        return "http://localhost:" + port + "/marginhotel/staff/receptionist";
    }

    @BeforeAll
    static void setUp() {
        Name name = new Name.Builder()
                .setFirstName("Lithabile")
                .setMiddleName("L")
                .setLastName("Lalela")
                .build();
        ContactDetails contactDetails = new ContactDetails.Builder()
                .setEmail("lithabile@marginhotel.com")
                .setMobile("0821234567")
                .build();
        manager = StaffFactory.createManager(1L, name, contactDetails, "Office 1A");
        receptionist = StaffFactory.createReceptionist(2L, name, contactDetails, "Desk 3B");
    }

    // ── MANAGER TESTS ──────────────────────────────

    @Test
    @Order(1)
    void createManager() {
        String url = MANAGER_URL() + "/create";
        ResponseEntity<Manager> response = this.restTemplate.postForEntity(
                url, manager, Manager.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        managerId = response.getBody().getStaffId();
        System.out.println("Created Manager: " + response.getBody());
    }

    @Test
    @Order(2)
    void readManager() {
        String url = MANAGER_URL() + "/read/" + managerId;
        ResponseEntity<Manager> response = this.restTemplate.getForEntity(
                url, Manager.class);
        assertNotNull(response.getBody());
        System.out.println("Read Manager: " + response.getBody());
    }

    @Test
    @Order(3)
    void updateManager() {
        Name newName = new Name.Builder()
                .setFirstName("Updated")
                .setMiddleName("L")
                .setLastName("Lalela")
                .build();
        ContactDetails cd = new ContactDetails.Builder()
                .setEmail("updated@marginhotel.com")
                .setMobile("0821234567")
                .build();
        Manager updated = new Manager.Builder()
                .copy(manager)
                .setName(newName)
                .setContactDetails(cd)
                .setOfficeNumber("Office 2B")
                .build();
        String url = MANAGER_URL() + "/update";
        HttpEntity<Manager> entity = new HttpEntity<>(updated);
        ResponseEntity<Manager> response = this.restTemplate.exchange(
                url, HttpMethod.PUT, entity, Manager.class);
        assertNotNull(response.getBody());
        System.out.println("Updated Manager: " + response.getBody());
    }

    @Test
    @Order(4)
    @Disabled
    void deleteManager() {
        String url = MANAGER_URL() + "/delete/" + managerId;
        this.restTemplate.delete(url);
        ResponseEntity<Manager> response = this.restTemplate.getForEntity(
                MANAGER_URL() + "/read/" + managerId, Manager.class);
        assertNull(response.getBody());
        System.out.println("Deleted Manager");
    }

    @Test
    @Order(5)
    void getAllManagers() {
        String url = MANAGER_URL() + "/getall";
        ResponseEntity<Manager[]> response = this.restTemplate.getForEntity(
                url, Manager[].class);
        assertNotNull(response);
        System.out.println("Get All Managers");
        if (response.getBody() != null) {
            for (Manager m : response.getBody()) {
                System.out.println(m);
            }
        }
    }

    // ── RECEPTIONIST TESTS ─────────────────────────

    @Test
    @Order(6)
    void createReceptionist() {
        String url = RECEPTIONIST_URL() + "/create";
        ResponseEntity<Receptionist> response = this.restTemplate.postForEntity(
                url, receptionist, Receptionist.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        receptionistId = response.getBody().getStaffId();
        System.out.println("Created Receptionist: " + response.getBody());
    }

    @Test
    @Order(7)
    void readReceptionist() {
        String url = RECEPTIONIST_URL() + "/read/" + receptionistId;
        ResponseEntity<Receptionist> response = this.restTemplate.getForEntity(
                url, Receptionist.class);
        assertNotNull(response.getBody());
        System.out.println("Read Receptionist: " + response.getBody());
    }

    @Test
    @Order(8)
    void updateReceptionist() {
        Name newName = new Name.Builder()
                .setFirstName("Updated")
                .setMiddleName("L")
                .setLastName("Lalela")
                .build();
        ContactDetails cd = new ContactDetails.Builder()
                .setEmail("updated@marginhotel.com")
                .setMobile("0821234567")
                .build();
        Receptionist updated = new Receptionist.Builder()
                .copy(receptionist)
                .setName(newName)
                .setContactDetails(cd)
                .setDeskNumber("Desk 5A")
                .build();
        String url = RECEPTIONIST_URL() + "/update";
        HttpEntity<Receptionist> entity = new HttpEntity<>(updated);
        ResponseEntity<Receptionist> response = this.restTemplate.exchange(
                url, HttpMethod.PUT, entity, Receptionist.class);
        assertNotNull(response.getBody());
        System.out.println("Updated Receptionist: " + response.getBody());
    }

    @Test
    @Order(9)
    @Disabled
    void deleteReceptionist() {
        String url = RECEPTIONIST_URL() + "/delete/" + receptionistId;
        this.restTemplate.delete(url);
        ResponseEntity<Receptionist> response = this.restTemplate.getForEntity(
                RECEPTIONIST_URL() + "/read/" + receptionistId, Receptionist.class);
        assertNull(response.getBody());
        System.out.println("Deleted Receptionist");
    }

    @Test
    @Order(10)
    void getAllReceptionists() {
        String url = RECEPTIONIST_URL() + "/getall";
        ResponseEntity<Receptionist[]> response = this.restTemplate.getForEntity(
                url, Receptionist[].class);
        assertNotNull(response);
        System.out.println("Get All Receptionists");
        if (response.getBody() != null) {
            for (Receptionist r : response.getBody()) {
                System.out.println(r);
            }
        }
    }
}