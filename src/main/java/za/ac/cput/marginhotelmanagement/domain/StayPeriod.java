package za.ac.cput.marginhotelmanagement.domain;
/*
   Author: Katlego Malaka(230443370)
   Date: 20 June 2026 */

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Embeddable
public class StayPeriod implements ValueObject {

    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;

   public StayPeriod(LocalDate localDate, LocalDate date){

   }
    private StayPeriod(Builder builder) {
        this.checkInDate = builder.checkInDate;
        this.checkOutDate = builder.checkOutDate;
    }

    public StayPeriod() {

    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    @Override
    public String toString() {
        return "StayPeriod{" +
                "checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }
    public static class Builder{
        private LocalDateTime checkInDate;
        private LocalDateTime checkOutDate;

        public Builder setCheckInDate(LocalDateTime checkInDate) {
            this.checkInDate = checkInDate;
            return this;
        }

        public Builder setCheckOutDate(LocalDateTime checkOutDate) {
            this.checkOutDate = checkOutDate;
            return this;
        }
        public Builder copy(StayPeriod stayPeriod){
            this.checkInDate = stayPeriod.checkInDate;
            this.checkOutDate = stayPeriod.checkOutDate;
            return this;
        }
        public StayPeriod build(){
            return new StayPeriod(this);
        }
    }
}
