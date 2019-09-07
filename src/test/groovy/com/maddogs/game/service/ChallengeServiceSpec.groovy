package com.maddogs.game.service

import com.maddogs.game.model.Team
import com.maddogs.game.repository.TeamRepository
import com.maddogs.game.request.Challenge
import spock.lang.Specification

class ChallengeServiceSpec extends Specification {
    TeamRepository teamRepository = Mock(TeamRepository)
    ChallengeService challengeService = new ChallengeService(teamRepository)

    def "Test challenging a team"() {
        given: "A challenge"
        Challenge challenge = new Challenge()
        challenge.setChallengeNumber(6)
        challenge.setTeamId("test-team")

        and: "A team"
        Team team = Mock(Team)

        and: "A mock optional"
        Optional optional = Optional.of(team)

        and: "That team is returned by the repository"
        teamRepository.findById("test-team") >> optional

        when: "Challenge is called"
        challengeService.challenge(challenge)

        then: "The team has it's challenge number set"
        1 * team.setChallengeNumber(challenge.getChallengeNumber())

        and: "The team is saved"
        1 * teamRepository.save(team)
    }
}
