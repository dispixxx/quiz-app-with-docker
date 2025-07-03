package com.disp.questionservice.dao;


import com.disp.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);
//    Optional<Question> findQuestionById(Long id);

    @Query(value = "SELECT q.id FROM question q WHERE q.category = ?1 ORDER BY RANDOM() LIMIT ?2", nativeQuery = true)
    List<Long> findRandomQuestionsByCategory(String category, Integer numQ);
}
