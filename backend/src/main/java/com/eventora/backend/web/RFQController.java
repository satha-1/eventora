package com.eventora.backend.web;

import com.eventora.backend.domain.Event;
import com.eventora.backend.domain.RFQ;
import com.eventora.backend.domain.User;
import com.eventora.backend.repository.EventRepository;
import com.eventora.backend.repository.RFQRepository;
import com.eventora.backend.web.dto.RFQRequest;
import com.eventora.backend.web.dto.RFQResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rfqs")
@CrossOrigin(origins = "*")
public class RFQController {
    private final RFQRepository rfqRepo;
    private final EventRepository eventRepo;

    public RFQController(RFQRepository rfqRepo, EventRepository eventRepo) {
        this.rfqRepo = rfqRepo;
        this.eventRepo = eventRepo;
    }

    @PostMapping
    public ResponseEntity<RFQResponse> create(@AuthenticationPrincipal User requester, @RequestBody RFQRequest req){
        if (requester == null) return ResponseEntity.status(401).build();
        Event event = eventRepo.findById(req.eventId()).orElse(null);
        if (event == null) return ResponseEntity.notFound().build();
        RFQ r = new RFQ();
        r.setRequester(requester); r.setEvent(event); r.setDetails(req.details()); r.setStatus("OPEN");
        rfqRepo.save(r);
        return ResponseEntity.ok(new RFQResponse(r.getId(), event.getId(), r.getStatus(), r.getDetails()));
    }

    @GetMapping("/mine")
    public List<RFQResponse> myRfqs(@AuthenticationPrincipal User user){
        return rfqRepo.findByRequesterId(user.getId()).stream()
                .map(r -> new RFQResponse(r.getId(), r.getEvent().getId(), r.getStatus(), r.getDetails())).toList();
    }

    @GetMapping("/incoming")
    public List<RFQResponse> incomingForOrganizer(@AuthenticationPrincipal User user){
        return rfqRepo.findByEventOrganizerId(user.getId()).stream()
                .map(r -> new RFQResponse(r.getId(), r.getEvent().getId(), r.getStatus(), r.getDetails())).toList();
    }
}
