package com.eventora.backend.repository;
import com.eventora.backend.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReviewRepository extends JpaRepository<Review, Long> { }
