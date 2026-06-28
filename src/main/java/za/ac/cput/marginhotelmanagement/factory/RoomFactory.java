package za.ac.cput.marginhotelmanagement.factory;

import za.ac.cput.marginhotelmanagement.domain.Room;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import java.util.UUID;

public class RoomFactory {


    public static Room createRoom(int roomNumber, RoomType roomType, double pricePerNight, RoomStatus roomStatus) {
        // Validate core constraints before construction
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("Room number must be greater than zero.");
        }
        if (roomType == null) {
            throw new IllegalArgumentException("Room type cannot be null.");
        }
        if (pricePerNight < 0.0) {
            throw new IllegalArgumentException("Price per night cannot be negative.");
        }

        // Auto-generate a secure random unique identifier
        String generatedRoomId = UUID.randomUUID().toString();

        // Enforce a sensible runtime fallback default for status
        RoomStatus operationalStatus = (roomStatus == null) ? RoomStatus.AVAILABLE : roomStatus;

        // Instantiate via the Builder
        return new Room.Builder()
                .setRoomId(generatedRoomId)
                .setRoomNumber(roomNumber)
                .setRoomType(roomType)
                .setPricePerNight(pricePerNight)
                .setRoomStatus(operationalStatus)
                .build();
    }
}
