package za.ac.cput.marginhotelmanagement.domain.dtos.room;

import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;

public class RoomDto {
    private Long roomId;
    private int roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private RoomStatus roomStatus;

    private RoomDto(Builder builder) {
        this.roomId = builder.roomId;
        this.roomNumber = builder.roomNumber;
        this.roomType = builder.roomType;
        this.pricePerNight = builder.pricePerNight;
        this.roomStatus = builder.roomStatus;
    }

    public Long getRoomId() { return roomId; }
    public int getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public RoomStatus getRoomStatus() { return roomStatus; }

    public static class Builder {
        private Long roomId;
        private int roomNumber;
        private RoomType roomType;
        private double pricePerNight;
        private RoomStatus roomStatus;

        public Builder setRoomId(Long roomId) { this.roomId = roomId; return this; }
        public Builder setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; return this; }
        public Builder setRoomType(RoomType roomType) { this.roomType = roomType; return this; }
        public Builder setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; return this; }
        public Builder setRoomStatus(RoomStatus roomStatus) { this.roomStatus = roomStatus; return this; }

        public RoomDto build() { return new RoomDto(this); }
    }
}
