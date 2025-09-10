package com.eventora.backend.web;

import com.eventora.backend.domain.Event;
import com.eventora.backend.domain.Role;
import com.eventora.backend.domain.User;
import com.eventora.backend.repository.EventRepository;
import com.eventora.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DevDataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final PasswordEncoder encoder;

    public DevDataLoader(UserRepository userRepo, EventRepository eventRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.count() == 0) {
            User admin = new User(); admin.setName("Admin"); admin.setEmail("admin@eventora.lk");
            admin.setPassword(encoder.encode("admin123")); admin.setRole(Role.ADMIN);
            User organizer = new User(); organizer.setName("Organizer One"); organizer.setEmail("org1@eventora.lk");
            organizer.setPassword(encoder.encode("password")); organizer.setRole(Role.ORGANIZER);
            userRepo.saveAll(List.of(admin, organizer));

            Event e1 = new Event();
            e1.setTitle("Colombo Nightout Party"); e1.setDescription("DJ, drinks, and dance");
            e1.setCategory("Nightouts"); e1.setSubcategory("Party"); e1.setLocation("Colombo 03");
            e1.setStartAt(LocalDateTime.now().plusDays(7)); e1.setEndAt(LocalDateTime.now().plusDays(7).plusHours(4));
            e1.setPrice(5000); e1.setCapacity(200); e1.setOrganizer(organizer);

            Event e2 = new Event();
            e2.setTitle("Wedding Package @ Galle"); e2.setDescription("Full service: hall + catering + photography");
            e2.setCategory("Weddings"); e2.setSubcategory("Package"); e2.setLocation("Galle");
            e2.setStartAt(LocalDateTime.now().plusMonths(1)); e2.setEndAt(LocalDateTime.now().plusMonths(1).plusHours(6));
            e2.setPrice(1000000); e2.setCapacity(300); e2.setOrganizer(organizer);

            eventRepo.saveAll(List.of(e1, e2));
        }
    }
}
