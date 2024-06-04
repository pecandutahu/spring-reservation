package com.example.reservation.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reservation.models.Reservation;

// Buat Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    // Cari berdasarkan reservation date
    List<Reservation> findByReservationDate(LocalDate reservationDate);
}
