package com.quiz.repository;

import com.quiz.models.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * MongoDB Repository for Quiz entity
 */
@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
    // Custom queries can be added here if needed
}
