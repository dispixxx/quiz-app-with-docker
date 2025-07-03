package com.disp.quizservice.controller;


import com.disp.quizservice.dto.QuizDto;
import com.disp.quizservice.model.QuestionWrapper;
import com.disp.quizservice.model.Response;
import com.disp.quizservice.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz/")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    //to Question-service
    @PostMapping("create")
    public ResponseEntity<String> createQuiz(
            @RequestBody QuizDto quizDto
    ) {
        return quizService.createQuiz(quizDto.category(),
                quizDto.numQuestions(),
                quizDto.title());

    }

    //to Question-service
    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuiz(@PathVariable Long id) {
        return quizService.getQuizQuestions(id);
    }
    //to Question-service
    @PostMapping("submit/{id}")
    public ResponseEntity<Double> submitQuiz(
            @PathVariable Long id,
            @RequestBody List<Response> responses) {
        return quizService.calculateResult(id,responses);
    }

}
