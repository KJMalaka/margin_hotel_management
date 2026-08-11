package za.ac.cput.marginhotelmanagement.service;
/*
    IGuestService.java
    Service interface for Guest entity
    Author: Hlomla Magopeni (218070349)
    Date: 16 July 2026
*/

import za.ac.cput.marginhotelmanagement.domain.Guest;

import java.util.List;

public interface IGuestService extends IService<Guest, Long> {
    List<Guest> findByFirstName(String firstName);
    List<Guest> findByLastName(String lastName);
    Guest findByEmail(String email);
}