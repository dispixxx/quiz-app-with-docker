package com.disp.quizservice.feign;

import com.disp.quizservice.model.QuestionWrapper;
import com.disp.quizservice.model.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "QUESTION-SERVICE")
public interface QuizInterface {

    //From Question-service
    @GetMapping("question/generate")
    ResponseEntity<List<Long>> generateQuestionQuiz(
            @RequestParam String category,
            @RequestParam Integer numQuestions);

    //From Question-service
    @PostMapping("question/getQuestions")
    ResponseEntity<List<QuestionWrapper>> getQuestions(
            @RequestBody List<Long> questionIds);

    //From Question-service
    @PostMapping("question/getScore")
    ResponseEntity<Double> getScore(
            @RequestBody List<Response> responses);
}
