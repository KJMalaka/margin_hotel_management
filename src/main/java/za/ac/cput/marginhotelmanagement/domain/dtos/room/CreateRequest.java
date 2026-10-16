package za.ac.cput.marginhotelmanagement.domain.dtos.room;

import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;

public class CreateRequest {
    private int roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private RoomStatus roomStatus;

    public CreateRequest() {
    }

    public CreateRequest(int roomNumber, RoomType roomType, double pricePerNight, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.roomStatus = roomStatus;
    }

    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public RoomStatus getRoomStatus() { return roomStatus; }
    public void setRoomStatus(RoomStatus roomStatus) { this.roomStatus = roomStatus; }
}
