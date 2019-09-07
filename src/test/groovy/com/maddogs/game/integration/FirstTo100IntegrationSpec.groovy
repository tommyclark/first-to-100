package com.maddogs.game

import com.fasterxml.jackson.databind.ObjectMapper
import com.maddogs.game.model.Question
import com.maddogs.game.model.Team
import com.maddogs.game.repository.QuestionRepository
import com.maddogs.game.repository.TeamRepository
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
    TeamRepository teamRepository

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    ObjectMapper mapper

    Question question

    Team team

    def setup() {
        team = new Team()
        team.setName("yo")
        team.setChallengeNumber(2)
        teamRepository.saveAndFlush(team)

        question = new Question()
        question.setQuestion("Breakfast items?")
        question.setAnswers(new ArrayList<>(Arrays.asList("bacon", "sausages", "egg")))
        questionRepository.saveAndFlush(question)
    }

    def cleanup() {
        teamRepository.deleteAll()
        questionRepository.deleteAll()
    }

    def "Test sending a challenge"() {
        when: "request is sent"
        def response = post("/challenge", """
            { 
                "teamId": "${team.getTeamId()}",
                "challengeNumber": 5
            }
        """)

        then: "a status code of 200 is returned"
        response.status == 200

        and: "The challenge number has been set"
        teamRepository.findById(team.getTeamId()).get().getChallengeNumber() == 5
    }

    def "Test getting team with highest challenge number"() {
        given: "A higher challenging team"
        Team braveTeam = new Team()
        braveTeam.setName("bigger")
        braveTeam.setChallengeNumber(7)
        teamRepository.saveAndFlush(braveTeam)

        when: "request is sent"
        def response = get("/challenge")

        then: "a status code of 200 is returned"
        response.status == 200

        and: "The bravest team is returned"
        mapper.writeValueAsString(braveTeam) == response.getBody()
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
        given: "A team has a certain amount of points"
        int points = teamRepository.findById(team.getTeamId()).get().getPoints()

        when: "request is sent"
        def response = post("/question/" + question.getQuestionId() + "/team/" + team.getTeamId(),
                """
        {
            "answer": "Sausages"
        }
        """)

        then: "a status code of 202 is returned"
        response.status == 202

        and: "The team gains a point"
        teamRepository.findById(team.getTeamId()).get().getPoints() == points + 1
    }

    def "Test answering a question incorrectly"() {
        given: "A team has a certain amount of points"
        int points = teamRepository.findById(team.getTeamId()).get().getPoints()

        when: "request is sent"
        def response = post("/question/" + question.getQuestionId() + "/team/" + team.getTeamId(),
                """
        {
            "answer": "nothing"
        }
        """)

        then: "a status code of 405 is returned"
        response.status == 406

        and: "The team gains a point"
        teamRepository.findById(team.getTeamId()).get().getPoints() == points
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
