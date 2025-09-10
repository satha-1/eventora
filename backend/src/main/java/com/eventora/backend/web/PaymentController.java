package com.eventora.backend.web;

import com.eventora.backend.domain.Payment;
import com.eventora.backend.domain.Registration;
import com.eventora.backend.repository.PaymentRepository;
import com.eventora.backend.repository.RegistrationRepository;
import com.eventora.backend.web.dto.PaymentInitRequest;
import com.eventora.backend.web.dto.PaymentInitResponse;
import com.eventora.backend.web.dto.PaymentWebhookRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    private final PaymentRepository payRepo;
    private final RegistrationRepository regRepo;

    public PaymentController(PaymentRepository payRepo, RegistrationRepository regRepo) {
        this.payRepo = payRepo;
        this.regRepo = regRepo;
    }

    @PostMapping("/init")
    public ResponseEntity<PaymentInitResponse> init(@RequestBody PaymentInitRequest req){
        Registration r = regRepo.findById(req.registrationId()).orElse(null);
        if (r == null) return ResponseEntity.notFound().build();
        String ref = "PH-" + UUID.randomUUID();
        Payment p = new Payment();
        p.setRegistration(r); p.setProvider("PAYHERE"); p.setStatus("INITIATED"); p.setAmount(req.amount()); p.setExternalRef(ref);
        payRepo.save(p);
        // In real integration, construct the PayHere sandbox URL with merchant and return URL params
        String fakeRedirect = "https://sandbox.payhere.lk/pay?ref=" + ref + "&amount=" + req.amount();
        return ResponseEntity.ok(new PaymentInitResponse("PAYHERE", fakeRedirect, ref));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody PaymentWebhookRequest req){
        // In real scenario, validate signature and update status accordingly
        var payment = payRepo.findAll().stream().filter(p -> req.reference().equals(p.getExternalRef())).findFirst().orElse(null);
        if (payment == null) return ResponseEntity.notFound().build();
        payment.setStatus(req.status());
        payRepo.save(payment);
        var reg = payment.getRegistration();
        if ("PAID".equals(req.status())) {
            reg.setStatus("CONFIRMED");
            regRepo.save(reg);
        }
        return ResponseEntity.ok().build();
    }
}
