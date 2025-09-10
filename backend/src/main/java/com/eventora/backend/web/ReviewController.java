package com.eventora.backend.web;

import com.eventora.backend.domain.Event;
import com.eventora.backend.domain.Review;
import com.eventora.backend.domain.User;
import com.eventora.backend.repository.EventRepository;
import com.eventora.backend.repository.ReviewRepository;
import com.eventora.backend.web.dto.ReviewRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;

    public ReviewController(ReviewRepository reviewRepository, EventRepository eventRepository) {
        this.reviewRepository = reviewRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity<Void> leave(@AuthenticationPrincipal User user, @RequestBody ReviewRequest req) {
        if (user == null) return ResponseEntity.status(401).build();
        Event e = eventRepository.findById(req.eventId()).orElse(null);
        if (e == null) return ResponseEntity.notFound().build();
        Review rv = new Review();
        rv.setEvent(e); rv.setUser(user); rv.setRating(req.rating()); rv.setComment(req.comment());
        reviewRepository.save(rv);
        return ResponseEntity.ok().build();
    }
}
