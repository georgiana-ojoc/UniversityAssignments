package gomoku.repositories;

import gomoku.models.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    boolean existsByUsername(String username);

    Player findByUsername(String username);

    void deleteByUsername(String username);
}
