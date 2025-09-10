package com.eventora.backend.repository;
import com.eventora.backend.domain.RFQ;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface RFQRepository extends JpaRepository<RFQ, Long> {
    List<RFQ> findByRequesterId(Long requesterId);
    List<RFQ> findByEventOrganizerId(Long organizerId);
}
