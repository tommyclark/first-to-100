package com.maddogs.game.service;

import com.maddogs.game.model.Team;
import com.maddogs.game.repository.TeamRepository;
import com.maddogs.game.request.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {
    private TeamRepository teamRepository;

    @Autowired
    public ChallengeService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public void challenge(Challenge challenge) {
        Team team = teamRepository.findById(challenge.getTeamId()).get();
        team.setChallengeNumber(challenge.getChallengeNumber());
        teamRepository.save(team);
    }
}
