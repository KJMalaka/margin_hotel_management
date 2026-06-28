package za.ac.cput.marginhotelmanagement.factory;
/* ContactDetailsFactoryTest.java
   TDD Factory Test for ContactDetails
   Author: Lithabile Lalela (221340963)
   Date: 28 June 2026 */

import org.junit.jupiter.api.*;
import za.ac.cput.marginhotelmanagement.domain.ContactDetails;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactDetailsFactoryTest {

    @Test
    @Order(1)
    void createContactDetailsSuccess() {
        ContactDetails cd = ContactDetailsFactory.createContactDetails(
                "lithabile@marginhotel.com", "0678835765");
        assertNotNull(cd);
        assertEquals("lithabile@marginhotel.com", cd.getEmail());
        assertEquals("0678835765", cd.getMobile());
        System.out.println("ContactDetails created successfully: " + cd);
    }

    @Test
    @Order(2)
    void createContactDetailsWithInvalidEmail() {
        ContactDetails cd = ContactDetailsFactory.createContactDetails(
                "Lihabileemail", "0678835765");
        assertNull(cd);
        System.out.println("ContactDetails creation failed due to invalid email.");
    }

    @Test
    @Order(3)
    void createContactDetailsWithNullEmail() {
        ContactDetails cd = ContactDetailsFactory.createContactDetails(
                null, "0678835765");
        assertNull(cd);
        System.out.println("ContactDetails creation failed due to null email.");
    }

    @Test
    @Order(4)
    void createContactDetailsWithInvalidMobile() {
        ContactDetails cd = ContactDetailsFactory.createContactDetails(
                "lithabile@marginhotel.com", "06854799");
        assertNull(cd);
        System.out.println("ContactDetails creation failed due to invalid mobile.");
    }

    @Test
    @Order(5)
    void createContactDetailsWithNullMobile() {
        ContactDetails cd = ContactDetailsFactory.createContactDetails(
                "lithabile@marginhotel.com", null);
        assertNull(cd);
        System.out.println("ContactDetails creation failed due to null mobile.");
    }
}