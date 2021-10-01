package com.deviget.repository;

import com.deviget.domain.GameState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends CrudRepository<GameState, Long> {
    GameState findFirstByGameIdEquals(String gameId);
}
