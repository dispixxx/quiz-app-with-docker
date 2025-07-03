package com.disp.quizservice.dto;

public record QuizDto(
        String category,
        Integer numQuestions,
        String title
) {
}
