package gomoku.services;

import gomoku.dtos.rooms.RoomCreateDTO;
import gomoku.dtos.rooms.RoomDTO;
import gomoku.dtos.rooms.RoomUpdateDTO;
import gomoku.exceptions.RoomNotFoundException;
import gomoku.models.Room;
import gomoku.repositories.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModelMapper modelMapper;

    public RoomDTO create(RoomCreateDTO createdRoom) {
        Room room = new Room();
        BeanUtils.copyProperties(createdRoom, room);
        roomRepository.save(room);
        return modelMapper.map(room, RoomDTO.class);
    }

    public RoomDTO getByRandomIdentifier(String randomIdentifier) throws RoomNotFoundException {
        Room room = roomRepository.findByRandomIdentifier(randomIdentifier);
        if (room == null) {
            throw new RoomNotFoundException(randomIdentifier);
        }
        return modelMapper.map(room, RoomDTO.class);
    }

    public List<RoomDTO> getAll() {
        List<Room> rooms = (List<Room>) roomRepository.findAll();
        List<RoomDTO> result = new ArrayList<>();
        for (Room room : rooms) {
            result.add(modelMapper.map(room, RoomDTO.class));
        }
        return result;
    }

    public RoomUpdateDTO update(RoomUpdateDTO updatedRoom, String randomIdentifier) throws RoomNotFoundException {
        Room room = roomRepository.findByRandomIdentifier(randomIdentifier);
        if (room == null) {
            throw new RoomNotFoundException(randomIdentifier);
        }
        BeanUtils.copyProperties(updatedRoom, room);
        roomRepository.save(room);
        return updatedRoom;
    }

    public void delete(String randomIdentifier) throws RoomNotFoundException {
        if (!roomRepository.existsByRandomIdentifier(randomIdentifier)) {
            throw new RoomNotFoundException(randomIdentifier);
        }
        roomRepository.deleteByRandomIdentifier(randomIdentifier);
    }
}
