package com.example.reservation.services;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reservation.models.Reservation;
import com.example.reservation.repositories.ReservationRepository;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(String customerName, LocalDate reservationDate) {
        // Validasi tanggal reservasi, apaka dilakukan di hari libur atau tidak
        if (isHoliday(reservationDate)) {
            throw new IllegalArgumentException("Reservation cannot be made on holidays (Wednesday and Friday).");
        }

        List<Reservation> reservations = reservationRepository.findByReservationDate(reservationDate);
        // Validasi apakah reservasi dalam satu hari sudah mencapai 2
        if (reservations.size() >= 2) {
            throw new IllegalArgumentException("Maximum number of reservations reached for this date. Only 2 reservations are allowed per day.");
        }

        // Simpan data
        Reservation reservation = new Reservation();
        reservation.setCustomerName(customerName);
        reservation.setReservationDate(reservationDate);
        return reservationRepository.save(reservation);
    }

    // Menampilkan datta reservasi dalam satu minggu
    // Waktu satu minggu akan dihitung dari start date yang diinput oleh user
    // Satu minggu diartikan sebagai 7 hari
    public List<Reservation> getReservationsForWeek(LocalDate startDate){
        return Stream.iterate(startDate, date -> date.plusDays(1)) //lakukan iterasi dalam satuan hari
                .limit(7) // Limit iterasi pada iterasi ke 7
                .filter(date -> !isHoliday(date)) // filter data yang bukan di hari libur
                .flatMap(date -> reservationRepository.findByReservationDate(date).stream()) //tampilkan data berdasarkan date yang ada pada iterasi
                .collect(Collectors.toList());
    }

    // Cek apakah date yang diinputkan adalah hari libur
    private boolean isHoliday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek(); // definisikan date yang diinput menjadi hari
        return dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY; // bandingkan dan kirim balikan dalam bentuk boolean
    }
}
