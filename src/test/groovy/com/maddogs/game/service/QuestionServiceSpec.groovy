package com.maddogs.game.service

import com.maddogs.game.model.Question
import com.maddogs.game.repository.QuestionRepository
import spock.lang.Specification

class QuestionServiceSpec extends Specification {
    QuestionRepository questionRepository = Mock(QuestionRepository)
    QuestionService questionService = new QuestionService(questionRepository)

    def "Testing answer when correct"() {
        given: "A question"
        Question question = new Question()

        and: "The question has some answers"
        question.setAnswers(new ArrayList<>(Arrays.asList("bacon", "sausages", "egg")))

        and: "A mock optional"
        Optional optional = Optional.of(question)

        and: "That question is returned by the repository"
        questionRepository.findById("test-question-id") >> optional

        when: "We check if an answer is correct"
        boolean correct = questionService.isAnswerCorrect("test-question-id", "bacon")

        then: "We can see the answer was correct"
        correct
    }

    def "Testing answer when correct but cases are different"() {
        given: "A question"
        Question question = new Question()

        and: "The question has some answers"
        question.setAnswers(new ArrayList<>(Arrays.asList("Bacon", "Sausages", "Egg")))

        and: "A mock optional"
        Optional optional = Optional.of(question)

        and: "That question is returned by the repository"
        questionRepository.findById("test-question-id") >> optional

        when: "We check if an answer is correct"
        boolean correct = questionService.isAnswerCorrect("test-question-id", "bacon")

        then: "We can see the answer was correct"
        correct
    }

    def "Testing answer when incorrect"() {
        given: "A question"
        Question question = new Question()

        and: "The question has some answers"
        question.setAnswers(new ArrayList<>(Arrays.asList("bacon", "sausages", "egg")))

        and: "A mock optional"
        Optional optional = Optional.of(question)

        and: "That question is returned by the repository"
        questionRepository.findById("test-question-id") >> optional

        when: "We check if an answer is correct"
        boolean correct = questionService.isAnswerCorrect("test-question-id", "cake")

        then: "We can see the answer was correct"
        !correct
    }

    def "Test getting random question"() {
        given: "A list of questions"
        List<Question> questions = new ArrayList<Question>()

        and: "The list has a question in it"
        Question question = new Question()
        questions.add(question)

        and: "The repository returns the list"
        questionRepository.retrieveQuestionsInRandomOrder() >> questions

        when: "Question service is called"
        Question result = questionService.getRandomQuestion()

        then: "The first item of the list is returned"
        result == question
    }
}
