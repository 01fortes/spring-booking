package com.jsamkt.learn.booking.repository;

import com.jsamkt.learn.booking.model.Booking;
import com.jsamkt.learn.booking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByRoom(Room room);
    List<Booking> findByUserIdAndRoom(String userId, Room room);

    @Query("SELECT b FROM Booking b WHERE b.room = :room AND " +
           "((b.startDate <= :endDate AND b.endDate >= :startDate))")
    List<Booking> findOverlappingBookings(Room room, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT * FROM bookings WHERE user_id = :userId AND start_date >= :startDateAfter AND end_date <= :endDateBefore", nativeQuery = true)
    List<Booking> findByUserIdAndStartDateIsAfterAndEndDateIsBefore(String userId, LocalDateTime startDateAfter, LocalDateTime endDateBefore);

    @Query(value = "SELECT b FROM Booking b WHERE b.room = :room AND b.startDate >= :endDateIsGreaterThan AND b.endDate <= :startDateIsLessThan")
    List<Booking> findByRoomAndStartDateIsLessThanEqualAndEndDateIsGreaterThanEqual(Room room, LocalDateTime startDateIsLessThan, LocalDateTime endDateIsGreaterThan);
}
