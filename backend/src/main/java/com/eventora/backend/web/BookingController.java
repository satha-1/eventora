package com.eventora.backend.web;

import com.eventora.backend.domain.Event;
import com.eventora.backend.domain.Registration;
import com.eventora.backend.domain.User;
import com.eventora.backend.repository.EventRepository;
import com.eventora.backend.repository.RegistrationRepository;
import com.eventora.backend.web.dto.BookingRequest;
import com.eventora.backend.web.dto.BookingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;

    public BookingController(RegistrationRepository registrationRepository, EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@AuthenticationPrincipal User attendee, @RequestBody BookingRequest req) {
        if (attendee == null) return ResponseEntity.status(401).build();
        Event e = eventRepository.findById(req.eventId()).orElse(null);
        if (e == null) return ResponseEntity.notFound().build();
        Registration r = new Registration();
        r.setAttendee(attendee);
        r.setEvent(e);
        r.setPackageType(req.packageType());
        r.setStatus("CONFIRMED"); // mock confirmation
        registrationRepository.save(r);
        return ResponseEntity.ok(new BookingResponse(r.getId(), r.getStatus()));
    }
}
