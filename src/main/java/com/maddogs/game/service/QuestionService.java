package com.maddogs.game.service;

import com.maddogs.game.model.Question;
import com.maddogs.game.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public boolean isAnswerCorrect(String questionId, String answer) {
        Question question = questionRepository.findById(questionId).get();
        return question.getAnswers().stream()
                .map(String::toLowerCase)
                .anyMatch(str -> str.equals(answer.toLowerCase()));
    }

    public Question getRandomQuestion() {
        return questionRepository.retrieveQuestionsInRandomOrder().get(0);
    }
}
