package com.aitravel.application.controller;

import com.aitravel.application.dto.requestdtos.NewsLetterRequest;
import com.aitravel.application.model.NewsLetter;
import com.aitravel.application.service.NewsLetterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/newsletter")
@CrossOrigin(origins = "*")
public class NewsLetterController {

    private final NewsLetterService newsLetterService;

    public NewsLetterController(NewsLetterService newsLetterService) {
        this.newsLetterService = newsLetterService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<NewsLetter> subscribeToNewsletter(@Valid @RequestBody NewsLetterRequest request) {
        NewsLetter subscriber = newsLetterService.subscribe(request.getEmail());
        return ResponseEntity.ok(subscriber);
    }
}
