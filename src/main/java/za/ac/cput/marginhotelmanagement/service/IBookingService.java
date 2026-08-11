package za.ac.cput.marginhotelmanagement.service;
/*
   Author: Katlego Malaka (230443370)
   Date: 09 July 2026
*/

import za.ac.cput.marginhotelmanagement.domain.Booking;

import java.util.List;

public interface IBookingService extends IService<Booking, Long> {
    boolean delete(Long id);

    List<Booking> getAll();
}
