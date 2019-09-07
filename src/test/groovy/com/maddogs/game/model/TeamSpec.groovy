package com.maddogs.game.model

import spock.lang.Specification

class TeamSpec extends Specification {
    def "Test incrementing points"() {
        given: "A team"
        Team team = new Team()

        and: "The team has 3 points"
        team.points = 3

        when: "Points are incremented"
        team.incrementPoints()

        then: "The team has 4 points"
        team.points == 4
    }

    def "Test incrementing total points"() {
        given: "A team"
        Team team = new Team()

        and: "The team has 3 points"
        team.totalPoints = 3

        when: "Points are incremented"
        team.incrementTotalPoints()

        then: "The team has 4 points"
        team.totalPoints == 4
    }
}
