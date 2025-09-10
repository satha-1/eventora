package com.eventora.backend.domain;

import jakarta.persistence.*;

@Entity
public class Proposal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RFQ rfq;

    @ManyToOne
    private User vendor;

    private Integer price; // LKR

    @Column(length = 2000)
    private String message;

    private String status; // SENT, ACCEPTED, DECLINED

    public Proposal() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RFQ getRfq() { return rfq; }
    public void setRfq(RFQ rfq) { this.rfq = rfq; }

    public User getVendor() { return vendor; }
    public void setVendor(User vendor) { this.vendor = vendor; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
