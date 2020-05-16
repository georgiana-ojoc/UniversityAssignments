package gomoku.services;

import gomoku.dtos.players.*;
import gomoku.exceptions.DuplicateUsernameException;
import gomoku.exceptions.PlayerNotFoundException;
import gomoku.models.Player;
import gomoku.repositories.PlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void create(PlayerCreateDTO createdPlayer) throws DuplicateUsernameException {
        String username = createdPlayer.getUsername();
        Player player = playerRepository.findByUsername(username);
        if (player != null) {
            throw new DuplicateUsernameException(username);
        }
        player = modelMapper.map(createdPlayer, Player.class);
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        playerRepository.save(player);
    }

    public PlayerCredentialsDTO getByUsername(String username) throws PlayerNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new PlayerNotFoundException(username);
        }
        return modelMapper.map(player, PlayerCredentialsDTO.class);
    }

    public List<PlayerGetDTO> getAll() {
        List<Player> players = (List<Player>) playerRepository.findAll();
        List<PlayerGetDTO> result = new ArrayList<>();
        for (Player player : players) {
            result.add(modelMapper.map(player, PlayerGetDTO.class));
        }
        return result;
    }

    public void update(PlayerUpdateDTO updatedPlayer, String username) throws PlayerNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new PlayerNotFoundException(username);
        }
        BeanUtils.copyProperties(updatedPlayer, player);
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        playerRepository.save(player);
    }

    public void delete(String username) throws PlayerNotFoundException {
        if (!playerRepository.existsByUsername(username)) {
            throw new PlayerNotFoundException(username);
        }
        playerRepository.deleteByUsername(username);
    }
}
