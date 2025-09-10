package com.eventora.backend.web;

import com.eventora.backend.domain.Event;
import com.eventora.backend.domain.User;
import com.eventora.backend.repository.EventRepository;
import com.eventora.backend.repository.UserRepository;
import com.eventora.backend.web.dto.EventRequest;
import com.eventora.backend.web.dto.EventResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    private final EventRepository eventRepository;
    @SuppressWarnings("unused")
    private final UserRepository userRepository;

    public EventController(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<EventResponse> list(@RequestParam(required = false) String category,
                                    @RequestParam(required = false) String q,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        var all = eventRepository.findAll(PageRequest.of(page, size)).stream().filter(e -> {
            boolean ok = true;
            if (category != null && !category.isBlank()) ok &= e.getCategory()!=null && e.getCategory().equalsIgnoreCase(category);
            if (q != null && !q.isBlank()) ok &= (e.getTitle()+" "+e.getDescription()+" "+e.getLocation()).toLowerCase().contains(q.toLowerCase());
            return ok;
        }).map(e -> new EventResponse(e.getId(), e.getTitle(), e.getDescription(), e.getCategory(), e.getSubcategory(),
                e.getLocation(), e.getStartAt(), e.getEndAt(), e.getPrice(), e.getCapacity(),
                e.getOrganizer()!=null ? e.getOrganizer().getName() : null)).toList();
        return all;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> get(@PathVariable Long id) {
        return eventRepository.findById(id)
                .map(e -> ResponseEntity.ok(new EventResponse(e.getId(), e.getTitle(), e.getDescription(), e.getCategory(), e.getSubcategory(),
                        e.getLocation(), e.getStartAt(), e.getEndAt(), e.getPrice(), e.getCapacity(),
                        e.getOrganizer()!=null ? e.getOrganizer().getName() : null)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@AuthenticationPrincipal User user, @RequestBody EventRequest req) {
        if (user == null) return ResponseEntity.status(401).build();
        Event e = new Event();
        e.setTitle(req.title()); e.setDescription(req.description());
        e.setCategory(req.category()); e.setSubcategory(req.subcategory());
        e.setLocation(req.location()); e.setStartAt(req.startAt()); e.setEndAt(req.endAt());
        e.setPrice(req.price()); e.setCapacity(req.capacity()); e.setOrganizer(user);
        eventRepository.save(e);
        return ResponseEntity.ok(new EventResponse(e.getId(), e.getTitle(), e.getDescription(), e.getCategory(), e.getSubcategory(),
                e.getLocation(), e.getStartAt(), e.getEndAt(), e.getPrice(), e.getCapacity(), user.getName()));
    }
}
