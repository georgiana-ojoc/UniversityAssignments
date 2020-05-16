package gomoku.controllers;


import gomoku.dtos.players.PlayerCreateDTO;
import gomoku.dtos.players.PlayerUpdateDTO;
import gomoku.exceptions.DuplicateUsernameException;
import gomoku.exceptions.PlayerNotFoundException;
import gomoku.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/players")
public class PlayersController {
    @Autowired
    private PlayerService playerService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PlayerCreateDTO player) throws DuplicateUsernameException {
        playerService.create(player);
        return new ResponseEntity<>("The player was created successfully.", HttpStatus.CREATED);
    }

    @GetMapping(value = "{username}")
    public ResponseEntity<?> getByUsername(@PathVariable String username) throws PlayerNotFoundException {
        return new ResponseEntity<>(playerService.getByUsername(username), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(playerService.getAll(), HttpStatus.OK);
    }

    @PutMapping(value = "{username}")
    public ResponseEntity<?> update(@RequestBody PlayerUpdateDTO player, @PathVariable String username)
            throws PlayerNotFoundException {
        playerService.update(player, username);
        return new ResponseEntity<>("The player was updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping(value = "{username}")
    public ResponseEntity<?> delete(@PathVariable String username) throws PlayerNotFoundException {
        playerService.delete(username);
        return new ResponseEntity<>("The player was deleted successfully.", HttpStatus.OK);
    }
}
