package za.ac.cput.marginhotelmanagement.factory;

import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;

public class RoomFactory {

    public static Room createRoom(int roomNumber, RoomType roomType, double pricePerNight, RoomStatus roomStatus) {
        // Enforce the database nullability rules at runtime
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be greater than zero.");
        }
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null.");
        }
        if (pricePerNight < 0.0) {
            throw new IllegalArgumentException("Price per night cannot be negative.");
        }

        // Apply a safe operational status default if none is provided
        RoomStatus operationalStatus = (roomStatus == null) ? RoomStatus.AVAILABLE : roomStatus;

        // Instantiate using the Builder pattern (Id will auto-generate on DB persist)
        return new Room.Builder()
                .setRoomNumber(roomNumber)
                .setRoomType(roomType)
                .setPricePerNight(pricePerNight)
                .setRoomStatus(operationalStatus)
                .build();
    }
}
