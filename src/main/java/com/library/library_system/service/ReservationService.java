package com.library.library_system.service;

import com.library.library_system.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public void makeReservation(Integer userId, Integer bookId) {
        // SP'yi çağırır. Stok varsa HOLD, yoksa WAITLIST yapar.
        reservationRepository.createReservation(userId, bookId);
    }
}