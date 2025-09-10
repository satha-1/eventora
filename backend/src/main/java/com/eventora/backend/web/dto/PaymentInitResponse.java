package com.eventora.backend.web.dto;
public record PaymentInitResponse(String provider, String redirectUrl, String reference) {}
