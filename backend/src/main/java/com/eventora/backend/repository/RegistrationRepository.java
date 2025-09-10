package com.eventora.backend.repository;
import com.eventora.backend.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RegistrationRepository extends JpaRepository<Registration, Long> { }
