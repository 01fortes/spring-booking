package com.jsamkt.learn.booking.repository;

import com.jsamkt.learn.booking.model.Room;
import com.jsamkt.learn.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByHotel(Hotel hotel);
    List<Room> findByHotelAndGuests(Hotel hotel, int guests);
    List<Room> findByWifiAndParking(boolean wifi, boolean parking);
}
