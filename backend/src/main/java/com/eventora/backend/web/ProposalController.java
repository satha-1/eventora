package com.eventora.backend.web;

import com.eventora.backend.domain.*;
import com.eventora.backend.repository.*;
import com.eventora.backend.web.dto.ProposalRequest;
import com.eventora.backend.web.dto.ProposalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proposals")
@CrossOrigin(origins = "*")
public class ProposalController {
    private final ProposalRepository proposalRepo;
    private final RFQRepository rfqRepo;
    private final RegistrationRepository regRepo;
    @SuppressWarnings("unused")
    private final EventRepository eventRepo;

    public ProposalController(ProposalRepository proposalRepo, RFQRepository rfqRepo, RegistrationRepository regRepo, EventRepository eventRepo) {
        this.proposalRepo = proposalRepo;
        this.rfqRepo = rfqRepo;
        this.regRepo = regRepo;
        this.eventRepo = eventRepo;
    }

    @PostMapping
    public ResponseEntity<ProposalResponse> send(@AuthenticationPrincipal User vendor, @RequestBody ProposalRequest req){
        if (vendor == null) return ResponseEntity.status(401).build();
        var rfq = rfqRepo.findById(req.rfqId()).orElse(null);
        if (rfq == null) return ResponseEntity.notFound().build();
        Proposal p = new Proposal();
        p.setRfq(rfq); p.setVendor(vendor);
        p.setPrice(req.price()); p.setMessage(req.message()); p.setStatus("SENT");
        proposalRepo.save(p);
        return ResponseEntity.ok(new ProposalResponse(p.getId(), rfq.getId(), p.getPrice(), p.getMessage(), p.getStatus()));
    }

    @GetMapping("/rfq/{rfqId}")
    public List<ProposalResponse> byRfq(@PathVariable Long rfqId){
        return proposalRepo.findByRfqId(rfqId).stream().map(p ->
                new ProposalResponse(p.getId(), p.getRfq().getId(), p.getPrice(), p.getMessage(), p.getStatus())
        ).toList();
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<ProposalResponse> accept(@AuthenticationPrincipal User user, @PathVariable Long id){
        var p = proposalRepo.findById(id).orElse(null);
        if (p == null) return ResponseEntity.notFound().build();
        p.setStatus("ACCEPTED"); proposalRepo.save(p);
        // create a confirmed registration for requester
        Registration r = new Registration();
        r.setAttendee(user);
        r.setEvent(p.getRfq().getEvent());
        r.setPackageType("proposal"); r.setStatus("PENDING_PAYMENT");
        regRepo.save(r);
        return ResponseEntity.ok(new ProposalResponse(p.getId(), p.getRfq().getId(), p.getPrice(), p.getMessage(), p.getStatus()));
    }
}
