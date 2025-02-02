package com.aitravel.application.repository;

import com.aitravel.application.model.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsLetterRepository extends JpaRepository<NewsLetter, Long> {
    boolean existsByEmail(String email);
}
