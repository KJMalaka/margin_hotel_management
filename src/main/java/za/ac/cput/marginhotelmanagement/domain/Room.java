package za.ac.cput.marginhotelmanagement.domain;

import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;

public class Room {

    private final String roomId;
    private final int roomNumber;
    private final RoomType roomType;
    private final double pricePerNight;
    private final RoomStatus roomStatus;


    public Room() {
        this.roomId = "";
        this.roomNumber = 0;
        this.roomType = null;
        this.pricePerNight = 0.0;
        this.roomStatus = null;
    }


    private Room(Builder builder) {
        this.roomId = builder.roomId;
        this.roomNumber = builder.roomNumber;
        this.roomType = builder.roomType;
        this.pricePerNight = builder.pricePerNight;
        this.roomStatus = builder.roomStatus;
    }


    public String getRoomId() {
        return roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }


    public static class Builder {
        private String roomId;
        private int roomNumber;
        private RoomType roomType;
        private double pricePerNight;
        private RoomStatus roomStatus;

        public Builder setRoomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder setRoomNumber(int roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public Builder setRoomType(RoomType roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder setPricePerNight(double pricePerNight) {
            this.pricePerNight = pricePerNight;
            return this;
        }

        public Builder setRoomStatus(RoomStatus roomStatus) {
            this.roomStatus = roomStatus;
            return this;
        }


        public Room build() {
            return new Room(this);
        }
    }
}
