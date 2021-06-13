package bowling.service;

import bowling.dto.PlayerDto;
import bowling.exception.ResourceNotFoundException;
import bowling.mapper.PlayerDtoMapper;
import bowling.mapper.PlayerMapper;
import bowling.model.Player;
import bowling.repository.PlayerRepository;
import com.dougestep.bowling.data.BowlingFrame;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerDtoMapper playerDtoMapper;
    private final PlayerMapper mapper;
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository, PlayerDtoMapper playerDtoMapper, PlayerMapper mapper) {
        this.playerRepository = playerRepository;
        this.playerDtoMapper = playerDtoMapper;
        this.mapper = mapper;
    }

    public List<Player> getAllPlayers() {

        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found: " + id));
    }

    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player player = playerDtoMapper.createPlayerFromDto(playerDto, new Player());
        if (playerExists(player) || playerExistsByPlayerNo(player.getPlayerNo())) {
            throw new EntityExistsException("Player already exists!");
        }

        return mapper.convertToDto(playerRepository.save(player));
    }

    public boolean playerExistsByPlayerNo(String playerNo) {
        Player playerToFind = playerRepository.findByPlayerNo(playerNo);

        return playerToFind != null;
    }

    private boolean playerExists(Player player) {
        Player playerToFind = playerRepository.findByFirstNameAndLastName(player.getFirstName(), player.getLastName());

        return playerToFind != null;
    }

    public BowlingFrame[] createFrames(String frames) {
        String[] eachFrame = frames.split(",");
        List<BowlingFrame> bowlingFrames = new ArrayList<>();

        for (String frame : eachFrame) {
            String frameWithoutParan = frame.substring(1, frame.length() - 1);
            String[] eachHit = frameWithoutParan.split(" ");

            BowlingFrame bowlingFrame = new BowlingFrame();
            if (eachHit.length == 1 && eachHit[0].contains("strike")) {
                bowlingFrames.add(BowlingFrame.strike());
                continue;
            } else if(eachHit.length == 1) {
                bowlingFrames.add(BowlingFrame.oneBall(Integer.parseInt(eachHit[0])));
                continue;
            } else {
                bowlingFrame.setFirstBall(Integer.parseInt(eachHit[0]));
                bowlingFrame.setSecondBall(Integer.parseInt(eachHit[1]));
                if (eachHit.length == 3) {
                    bowlingFrame.setSplit(Boolean.parseBoolean(eachHit[2]));
                }
            }

            bowlingFrames.add(bowlingFrame);
        }

        return bowlingFrames.toArray(new BowlingFrame[0]);
    }
}
