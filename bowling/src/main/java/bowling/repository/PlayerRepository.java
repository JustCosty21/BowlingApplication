package bowling.repository;

import bowling.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByFirstNameAndLastName(String firstName, String lastName);

    Player findByPlayerNo(String playerNo);
}
