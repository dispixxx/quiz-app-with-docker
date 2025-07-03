package com.disp.questionservice.controller;


import com.disp.questionservice.model.Question;
import com.disp.questionservice.model.QuestionWrapper;
import com.disp.questionservice.model.Response;
import com.disp.questionservice.service.QuestionService;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question/")
public class QuestionController {

    @Value("${server.port}")
    private int serverPort;

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        ResponseEntity<List<Question>> questions = questionService.getAllQuestions();
        return questions;
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionByCategory(@PathVariable("category") String category) {
        return questionService.getQuestionsByCategory(category);
    }

    //Один вопрос {}
    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    //Массив вопросов.[{},{},{}]
    @PostMapping("/addBatch")
    public ResponseEntity<String> addQuestions(@RequestBody List<Question> questions) {
        try {
            List<Question> savedQuestions = questionService.addQuestions(questions);
            return ResponseEntity.ok("Successfully added " + savedQuestions.size() + " questions");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding questions: " + e.getMessage());
        }
    }

    /*@PatchMapping("update/{id}")
    public String updateQuestion(@PathVariable("id") Long id, @RequestBody Question question) {
        return questionService.updateQuestion(id,question);
    }*/

    @DeleteMapping("delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id) {
        return questionService.deleteQuestion(id);
    }


    //Запросы от QuizMicroService
    @GetMapping("generate")
    public ResponseEntity<List<Long>> generateQuestionQuiz(
            @RequestParam String category,
            @RequestParam Integer numQuestions) {
        System.out.println("RequestFromQuizService");
        return questionService.getQuestionsForQuiz(category,numQuestions);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestions(@RequestBody List<Long> questionIds) {
        System.out.println(serverPort);
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("getScore")
    public ResponseEntity<Double> getScore(@RequestBody List<Response> responses) {
        return questionService.getScore(responses);
    }
}
