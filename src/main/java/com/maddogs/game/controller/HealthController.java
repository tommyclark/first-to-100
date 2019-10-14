package com.maddogs.game.controller;

import com.maddogs.game.model.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Question> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
