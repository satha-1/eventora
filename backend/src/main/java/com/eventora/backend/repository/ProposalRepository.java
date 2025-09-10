package com.eventora.backend.repository;
import com.eventora.backend.domain.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByRfqId(Long rfqId);
    List<Proposal> findByVendorId(Long vendorId);
}
