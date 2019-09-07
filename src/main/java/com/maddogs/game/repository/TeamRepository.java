package com.maddogs.game.repository;

import com.maddogs.game.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, String> {
    Team findFirstByOrderByChallengeNumberDesc();
}
