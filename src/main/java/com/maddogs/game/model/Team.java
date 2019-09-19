package com.maddogs.game.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Team {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    String teamId;

    @Column(unique = true)
    String name;

    int challengeNumber;

    int points;

    int totalPoints;

    public void incrementPoints() {
        points++;
    }

    public void incrementTotalPoints() {
        totalPoints++;
    }
}
