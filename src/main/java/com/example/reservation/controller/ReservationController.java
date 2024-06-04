package com.example.reservation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.reservation.models.Reservation;
import com.example.reservation.services.ReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {
        try {
            String customerName = reservation.getCustomerName();
            LocalDate reservationDate = reservation.getReservationDate();
            reservationService.createReservation(customerName, reservationDate);
            return new ResponseEntity<>("Reservation created successfully for " + customerName + " on " + reservationDate, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Reservation> getReservationsForWeek(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return reservationService.getReservationsForWeek(startDate);
    }
}

