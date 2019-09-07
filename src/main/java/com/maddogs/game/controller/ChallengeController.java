package com.maddogs.game.controller;

import com.maddogs.game.model.Team;
import com.maddogs.game.request.Challenge;
import com.maddogs.game.service.ChallengeService;
import com.maddogs.game.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/challenge")
public class ChallengeController {
    private ChallengeService challengeService;
    private TeamService teamService;

    @Autowired
    public ChallengeController(ChallengeService challengeService, TeamService teamService) {
        this.challengeService = challengeService;
        this.teamService = teamService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity postChallenge(@RequestBody Challenge input) {
        challengeService.challenge(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    ResponseEntity getBravestTeam() {
        Team team = teamService.getTeamWithHighestChallengeNumber();
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

}
