package com.aitravel.application.service;

import com.aitravel.application.model.NewsLetter;
import com.aitravel.application.repository.NewsLetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NewsLetterService {

    private final NewsLetterRepository newsLetterRepository;

    public NewsLetter subscribe(String email) {
        // Check if email already exists
        if (newsLetterRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already subscribed");
        }

        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setEmail(email);
        newsLetter.setCreatedAt(LocalDateTime.now());

        return newsLetterRepository.save(newsLetter);
    }
}
