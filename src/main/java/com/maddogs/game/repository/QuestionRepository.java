package com.maddogs.game.repository;

import com.maddogs.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, String> {
    @Query("SELECT u FROM Question u order by function('RAND')")
    List<Question> retrieveQuestionsInRandomOrder();

    Question findByQuestion(String question);
}
