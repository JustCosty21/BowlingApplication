package bowling.controller;

import bowling.dto.PlayerDto;
import bowling.model.Player;
import bowling.service.PlayerService;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/player")
public class CreatePlayerController {
    private final PlayerService playerService;

    public CreatePlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping()
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @PostMapping()
    public ResponseEntity<PlayerDto> createPlayer(@NotNull @RequestBody PlayerDto playerDto) {
        return ResponseEntity.ok(playerService.createPlayer(playerDto));
    }
}
