package com.disp.questionservice.service;


import com.disp.questionservice.dao.QuestionDao;
import com.disp.questionservice.model.Question;
import com.disp.questionservice.model.QuestionWrapper;
import com.disp.questionservice.model.Response;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);
    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            List<Question> questions = questionDao.findByCategory(category);
            return questions.isEmpty()
                    ? ResponseEntity.noContent().build() // 204 No Content
                    : ResponseEntity.ok(questions);
        } catch (Exception e) {

            log.info(("Error fetching questions for category: " + category + ", " + e.getMessage()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("success add", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("error add", HttpStatus.BAD_GATEWAY);
    }

    public String deleteQuestion(Long id) {
        questionDao.deleteById(id);
        return "success delete";
    }

    @Transactional
    public List<Question> addQuestions(List<Question> questions) {
        // Проверка на пустой список
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Question list cannot be empty");
        }

        // Валидация каждого вопроса
        for (Question question : questions) {
            if (question.getQuestionTitle() == null || question.getQuestionTitle().isBlank()) {
                throw new IllegalArgumentException("Question title cannot be empty");
            }
        }

        return questionDao.saveAll(questions);
    }

    //Запросы от QuizMicroService
    public ResponseEntity<List<Long>> getQuestionsForQuiz(String category, Integer numQuestions) {
        List<Long> questions = questionDao.findRandomQuestionsByCategory(category, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);

    }
    //Запросы от QuizMicroService
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Long> questionIds) {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for (Long qId : questionIds) {
            questions.add(questionDao.findById(qId).get());
        }

        questions.forEach(question -> {
            QuestionWrapper qw = new QuestionWrapper();
            qw.setId(question.getId());
            qw.setQuestionTitle(question.getQuestionTitle());
            qw.setOption1(question.getOption1());
            qw.setOption2(question.getOption2());
            qw.setOption3(question.getOption3());
            qw.setOption4(question.getOption4());
            questionWrappers.add(qw);
        });
        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }
    //Запросы от QuizMicroService
    public ResponseEntity<Double> getScore(List<Response> responses) {
        Double score = 0d; //Счетчик правильных ответов
        for (Response r : responses) {
            Question question = questionDao.findById(r.getId()).orElseThrow();
            if (r.getId().equals(question.getId())) {
                if (r.getResponse().equals(question.getRightAnswer())) {
                    score++;
                }
            } else {
                System.out.println("\u001B[31mLog: ID ответа:" + r.getId() + " != ID вопроса:" + question.getId() + "\u001B[0m");
                return new ResponseEntity<>(0d, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(score, HttpStatus.OK);

    }

    /*public String updateQuestion(Long id, Question question) {
        Optional<Question> existQuestion = questionDao.findQuestionById(id);
        if (existQuestion.isPresent()) {
            questionDao.save(existQuestion.get());
            return "success update";
        }else{
            return "error update";
        }
    }*/
}
