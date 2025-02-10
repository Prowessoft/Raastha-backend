package com.aitravel.application.service;

import com.aitravel.application.model.NewsLetter;
import com.aitravel.application.repositoryjpa.NewsLetterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsLetterService {

    private final NewsLetterRepository newsLetterRepository;

    public NewsLetter subscribe(String email) {
        log.info("Received subscription request for email: {}", email);
        // Check if email already exists
        if (newsLetterRepository.existsByEmail(email)) {
            log.error("Subscription failed. Email {} is already subscribed.", email);
            throw new RuntimeException("Email already subscribed");
        }

        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setEmail(email);
        newsLetter.setCreatedAt(LocalDateTime.now());
        log.debug("Creating new newsletter subscription for email: {}", email);

        NewsLetter savedNewsLetter = newsLetterRepository.save(newsLetter);
        log.info("Subscription successful for email: {} with newsletter id: {}", email, savedNewsLetter.getId());
        return savedNewsLetter;
    }
}
