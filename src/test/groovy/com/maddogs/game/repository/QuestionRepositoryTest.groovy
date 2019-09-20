package com.maddogs.game.repository

import com.maddogs.game.model.Question
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuestionRepositoryTest extends Specification {
    @Autowired
    QuestionRepository questionRepository

    def setup() {
        Question question = new Question()
        question.setQuestion("Breakfast items?")
        question.setAnswers(new ArrayList<>(Arrays.asList("bacon", "sausages", "egg")))
        questionRepository.saveAndFlush(question)

        Question altQuestion = new Question()
        altQuestion.setQuestion("Spurs players?")
        altQuestion.setAnswers(new ArrayList<>(Arrays.asList("klinsmann", "kane", "sheringham")))
        questionRepository.saveAndFlush(altQuestion)
    }

    def cleanup() {
        questionRepository.deleteAll()
    }

    def "Test finding a random question returns an actual question"() {
        when: "The repository is called"
        List<Question> question = questionRepository.retrieveQuestionsInRandomOrder()

        then: "Returns a list of these questions in a random order"
        question.size() == 2
    }
}
