package za.ac.cput.marginhotelmanagement.service;

import java.time.LocalDate;

public record GuestEmailStatus(boolean emailExists, LocalDate lastVisitDate) {
}
