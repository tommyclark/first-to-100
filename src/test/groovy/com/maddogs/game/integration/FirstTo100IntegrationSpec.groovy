package com.maddogs.game.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.maddogs.game.model.Question

import com.maddogs.game.repository.QuestionRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FirstTo100IntegrationSpec extends Specification {

    @Autowired
    TestRestTemplate testRestTemplate

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    ObjectMapper mapper

    Question question

    def setup() {
        question = new Question()
        question.setQuestion("Breakfast items?")
        question.setAnswers(new ArrayList<>(Arrays.asList("bacon", "sausages", "egg")))
        questionRepository.saveAndFlush(question)
    }

    def cleanup() {
        questionRepository.deleteAll()
    }

    def "Test getting a question"() {
        when: "request is sent"
        def response = get("/question")

        then: "a status code of 200 is returned"
        response.status == 200

        and: "The question is returned"
        mapper.writeValueAsString(question) == response.getBody()
    }

    def "Test answering a question"() {
        when: "request is sent"
        def response = post("/question/" + question.getQuestionId(),
                """
        {
            "answer": "Sausages"
        }
        """)

        then: "a status code of 202 is returned"
        response.status == 202
    }

    def "Test answering a question incorrectly"() {
        when: "request is sent"
        def response = post("/question/" + question.getQuestionId(),
                """
        {
            "answer": "nothing"
        }
        """)

        then: "a status code of 405 is returned"
        response.status == 406
    }

    def "Test the health endpoint"() {
        when: "request is sent"
        def response = get("/health")

        then: "a status code of 200 is returned"
        response.status == 200
    }

    def post(String path, String body) {
        HttpHeaders headers = getAuthHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<String> request = new HttpEntity<>(body, headers)
        return testRestTemplate.exchange(path, HttpMethod.POST, request, String.class)
    }

    def get(String path) {
        HttpHeaders headers = getAuthHeaders()
        HttpEntity<String> request = new HttpEntity<>(headers)
        return testRestTemplate.exchange(path, HttpMethod.GET, request, String.class)
    }

    HttpHeaders getAuthHeaders() {
        String plainCreds = "admin:password"
        byte[] plainCredsBytes = plainCreds.getBytes()
        byte[] base64CredsBytes = Base64.encoder.encode(plainCredsBytes)
        String base64Creds = new String(base64CredsBytes)

        HttpHeaders headers = new HttpHeaders()
        headers.add("Authorization", "Basic " + base64Creds)
        return headers
    }

}
