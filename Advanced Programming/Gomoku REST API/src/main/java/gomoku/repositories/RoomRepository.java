package gomoku.repositories;

import gomoku.models.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
    boolean existsByRandomIdentifier(String randomIdentifier);

    Room findByRandomIdentifier(String roomIdentifier);

    void deleteByRandomIdentifier(String randomIdentifier);
}
