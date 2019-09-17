package com.maddogs.game.controller;

import com.maddogs.game.model.Team;
import com.maddogs.game.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static java.util.Objects.isNull;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/team")
public class TeamController {
    private TeamRepository teamRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity postTeam(@RequestBody Team input) {
        teamRepository.saveAndFlush(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE, params = "id")
    ResponseEntity getTeam(@RequestParam String id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return new ResponseEntity<>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE, params = "name")
    ResponseEntity getTeamByName(@RequestParam String name) {
        Team team = teamRepository.findByName(name);

        if (isNull(team)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(team, HttpStatus.OK);
    }

}
