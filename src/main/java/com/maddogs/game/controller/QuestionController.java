package com.maddogs.game.controller;

import com.maddogs.game.model.Question;
import com.maddogs.game.request.Answer;
import com.maddogs.game.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/question")
public class QuestionController {
    private QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Question> getRandomQuestion() {
        Question question = questionService.getRandomQuestion();
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @RequestMapping(value="/{questionId}", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity postAnswer(@PathVariable String questionId, @RequestBody Answer input) {
        if (questionService.isAnswerCorrect(questionId, input.getAnswer())) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

}