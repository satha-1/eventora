package com.eventora.backend.domain;

import jakarta.persistence.*;

@Entity
public class Registration {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User attendee;
    @ManyToOne
    private Event event;
    private String packageType; // single, couple, family, VIP
    private String status; // PENDING, CONFIRMED, CANCELLED, PENDING_PAYMENT

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getAttendee() { return attendee; }
    public void setAttendee(User attendee) { this.attendee = attendee; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { this.packageType = packageType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
