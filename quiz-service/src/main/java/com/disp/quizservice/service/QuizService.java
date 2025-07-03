package com.disp.quizservice.service;


import com.disp.quizservice.dao.QuizDao;
import com.disp.quizservice.feign.QuizInterface;
import com.disp.quizservice.model.QuestionWrapper;
import com.disp.quizservice.model.Quiz;
import com.disp.quizservice.model.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizDao quizDao;

    private final QuizInterface quizInterface;

    public QuizService(QuizDao quizDao, QuizInterface quizInterface) {
        this.quizDao = quizDao;
        this.quizInterface = quizInterface;
    }

    //to Question-service
    public ResponseEntity<String> createQuiz(String category,
                                             int numQ,
                                             String tittle) {

//        List<Long> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        List<Long> questions = quizInterface.generateQuestionQuiz(category, numQ).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(tittle);
        quiz.setQuestionsIds(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz Created with id: " + quiz.getId(), HttpStatus.CREATED);
    }

    //to Question-service
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Long id) {
        Quiz quiz = quizDao.findById(id).orElseThrow();
        List<Long> questionsIds = quiz.getQuestionsIds();
        List<QuestionWrapper> questionWrappers = quizInterface.getQuestions(questionsIds).getBody();
        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    //to Question-service
    public ResponseEntity<Double> calculateResult(Long id, List<Response> responses) {
        Quiz currentQuiz = quizDao.findById(id).orElseThrow();
        List<Long> responseIds = responses.stream().map(Response::getId).toList();
        if (responseIds.equals(currentQuiz.getQuestionsIds())) {
            Double score = quizInterface.getScore(responses).getBody();
            return new ResponseEntity<>(score, HttpStatus.OK);
        } else {
            System.out.println("\u001B[31mLog: ID ответов != ID вопросов:" + "\u001B[0m\n"
                    + "ID ответов:" + responseIds + "\n" + "ID Вопросов:" + currentQuiz.getQuestionsIds() + "\u001B[0m");
            return new ResponseEntity<>(0.0, HttpStatus.BAD_REQUEST);
        }

    }
}
