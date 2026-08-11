package za.ac.cput.marginhotelmanagement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import za.ac.cput.marginhotelmanagement.enums.RoomStatus;
import za.ac.cput.marginhotelmanagement.enums.RoomType;
import java.util.Objects;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    private String roomId;
    private int roomNumber;
    private RoomType roomType;
    private double pricePerNight;
    private RoomStatus roomStatus;


    protected Room() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomId, room.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", pricePerNight=" + pricePerNight +
                ", roomStatus=" + roomStatus +
                '}';
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

        public Builder copy(Room room) {
            if (room != null) {
                this.roomId = room.roomId;
                this.roomNumber = room.roomNumber;
                this.roomType = room.roomType;
                this.pricePerNight = room.pricePerNight;
                this.roomStatus = room.roomStatus;
            }
            return this;
        }

        public Room build() {
            return new Room(this);
        }
    }
}