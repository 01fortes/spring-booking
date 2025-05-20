package com.jsamkt.learn.booking.repository;

import com.jsamkt.learn.booking.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    List<Hotel> findByLocation(String location);
    List<Hotel> findByNameContainingIgnoreCase(String name);
}
