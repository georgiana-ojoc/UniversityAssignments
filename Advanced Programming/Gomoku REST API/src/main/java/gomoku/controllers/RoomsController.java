package gomoku.controllers;

import gomoku.dtos.rooms.RoomCreateDTO;
import gomoku.dtos.rooms.RoomDTO;
import gomoku.dtos.rooms.RoomUpdateDTO;
import gomoku.exceptions.RoomNotFoundException;
import gomoku.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomsController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoomCreateDTO room) {
        return new ResponseEntity<>(roomService.create(room), HttpStatus.CREATED);
    }

    @GetMapping(value = "{randomIdentifier}")
    public ResponseEntity<?> getByRandomIdentifier(@PathVariable String randomIdentifier)
            throws RoomNotFoundException {
        return new ResponseEntity<>(roomService.getByRandomIdentifier(randomIdentifier), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(roomService.getAll(), HttpStatus.OK);
    }

    @PutMapping(value = "{randomIdentifier}")
    public ResponseEntity<?> update(@RequestBody RoomUpdateDTO room, @PathVariable String randomIdentifier)
            throws RoomNotFoundException {
        return new ResponseEntity<>(roomService.update(room, randomIdentifier), HttpStatus.OK);
    }

    @DeleteMapping(value = "{randomIdentifier}")
    public ResponseEntity<?> delete(@PathVariable String randomIdentifier) throws RoomNotFoundException {
        roomService.delete(randomIdentifier);
        return new ResponseEntity<>("The room was deleted successfully.", HttpStatus.OK);
    }
}
