package com.disp.quizservice.dao;


import com.disp.quizservice.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz,Long> {
}
