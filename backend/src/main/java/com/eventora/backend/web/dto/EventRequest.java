package com.eventora.backend.web.dto;
import java.time.LocalDateTime;
public record EventRequest(String title, String description, String category, String subcategory, String location,
                           LocalDateTime startAt, LocalDateTime endAt, Integer price, Integer capacity) {}
