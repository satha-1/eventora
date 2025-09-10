package com.eventora.backend.web.dto;
public record ReviewRequest(Long eventId, int rating, String comment) {}
