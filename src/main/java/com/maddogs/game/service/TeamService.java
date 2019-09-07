package com.maddogs.game.service;

import com.maddogs.game.model.Team;
import com.maddogs.game.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    private TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void incrementTeamPoints(String teamId) {
        Team team = teamRepository.findById(teamId).get();
        team.incrementPoints();
        teamRepository.save(team);
    }

    public void incrementTeamTotalPoints(String teamId) {
        Team team = teamRepository.findById(teamId).get();
        team.incrementTotalPoints();
        teamRepository.save(team);
    }

    public Team getTeamWithHighestChallengeNumber() {
        return teamRepository.findFirstByOrderByChallengeNumberDesc();
    }
}
