package bowling.repository;

import bowling.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    public Game findByGameName(String gameName);
}
