package za.ac.cput.marginhotelmanagement.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CheckUserEmailResponse {
    private boolean emailExists;
    private LocalDate lastVisitDate;
}
