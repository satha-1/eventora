package com.eventora.backend.repository;
import com.eventora.backend.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment, Long> { }
