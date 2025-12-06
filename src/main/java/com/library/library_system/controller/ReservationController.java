package com.library.library_system.controller;

import com.library.library_system.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/library/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    public String createReservation(@RequestParam Integer bookId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        try {
            reservationService.makeReservation(userId, bookId);
            redirectAttributes.addFlashAttribute("successMessage", "Success! Your reservation has been created.");
        } catch (Exception e) {
            // If a database error occurs (e.g., Already reserved)
            redirectAttributes.addFlashAttribute("errorMessage", "Error: The transaction could not be completed.");
            e.printStackTrace();
        }

        return "redirect:/library/user/home";
    }
}