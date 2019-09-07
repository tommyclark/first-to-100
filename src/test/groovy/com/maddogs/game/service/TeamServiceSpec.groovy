package com.maddogs.game.service

import com.maddogs.game.model.Team
import com.maddogs.game.repository.TeamRepository
import spock.lang.Specification

class TeamServiceSpec extends Specification {
    TeamRepository teamRepository = Mock(TeamRepository)
    TeamService teamService = new TeamService(teamRepository)

    def "Incrementing a teams points"() {
        given: "A team"
        Team team = Mock(Team)

        and: "A mock optional"
        Optional optional = Optional.of(team)

        and: "That team is returned by the repository"
        teamRepository.findById("test-team") >> optional

        when: "The teams points are incremented"
        teamService.incrementTeamPoints("test-team")

        then: "The teams points were incremented"
        1 * team.incrementPoints()

        and: "The team is saved to the db"
        1 * teamRepository.save(team)

    }

    def "Incrementing a teams total points"() {
        given: "A team"
        Team team = Mock(Team)

        and: "A mock optional"
        Optional optional = Optional.of(team)

        and: "That team is returned by the repository"
        teamRepository.findById("test-team") >> optional

        when: "The teams points are incremented"
        teamService.incrementTeamTotalPoints("test-team")

        then: "The teams points were incremented"
        1 * team.incrementTotalPoints()

        and: "The team is saved to the db"
        1 * teamRepository.save(team)

    }

    def "Get team with highest challenge number"() {
        when: "Service is called"
        teamService.getTeamWithHighestChallengeNumber()

        then: "the repository is called"
        1 * teamRepository.findFirstByOrderByChallengeNumberDesc()
    }
}
