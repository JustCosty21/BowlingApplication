package bowling.service;

import bowling.dto.GameDto;
import bowling.exception.ApplicationException;
import bowling.exception.ErrorCodes;
import bowling.exception.ErrorResponse;
import bowling.exception.ResourceNotFoundException;
import bowling.mapper.GameDtoMapper;
import bowling.mapper.GameMapper;
import bowling.model.Game;
import bowling.model.Player;
import bowling.repository.GameRepository;
import bowling.repository.PlayerRepository;
import com.dougestep.bowling.PrintManager;
import com.dougestep.bowling.data.Bowler;
import com.dougestep.bowling.data.BowlingFrame;
import com.dougestep.bowling.impl.GameManagerImpl;
import com.dougestep.bowling.impl.PrintManagerImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GameDtoMapper gameDtoMapper;
    private final GameMapper mapper;
    private final PlayerService playerService;

    public GameService(GameRepository gameRepository, GameDtoMapper gameDtoMapper,
                       GameMapper mapper, PlayerRepository playerRepository,
                       PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.gameDtoMapper = gameDtoMapper;
        this.mapper = mapper;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    public GameDto createGame(GameDto gameDto) {
        Game game = gameDtoMapper.createGameFromDto(gameDto, new Game());
        if (gameExists(game)) {
            throw new EntityExistsException("Game already exists!");
        }

        if (!canCreateGame(game.getPlayersNo())) {
            throw new ApplicationException(new ErrorResponse(
                    ErrorCodes.INTERNAL_ERROR,
                    "An error occured while trying to create the game " + game.getGameName() + ". Player not found!"));
        }

        return mapper.convertToDto(gameRepository.save(game));
    }

    private boolean canCreateGame(String playersNo) {
        for (String playerNo : playersNo.split(" ")) {
            if (!playerService.playerExistsByPlayerNo(playerNo)) {
                return false;
            }
        }

        return true;
    }

    private boolean gameExists(Game game) {
        Game gameToFind = gameRepository.findByGameName(game.getGameName());

        return gameToFind != null;
    }

    public byte[] playGame(Long id) throws IOException {
        Game game = gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game not found: " + id));
        List<com.dougestep.bowling.data.Game> games = new ArrayList<>();

        String playersNo = game.getPlayersNo();
        for (String playerNo : playersNo.split(" ")) {
            Player player = getPlayer(playerNo);
            Bowler playerBowler = new Bowler().setFirstName(player.getFirstName()).setLastName(player.getLastName());
            BowlingFrame[] frames = playerService.createFrames(player.getFrames());
            com.dougestep.bowling.data.Game gameDoug = GameManagerImpl.newGame(playerBowler).addFrames(frames).getGame();
            games.add(gameDoug);
        }

        PrintManager printManager = PrintManagerImpl.newInstance();

        PrintStream fileOut = new PrintStream("./out.txt");
        printManager.printGames(games.toArray(new com.dougestep.bowling.data.Game[0]), fileOut);

        InputStream targetStream = new FileInputStream("./out.txt");

        return IOUtils.toByteArray(targetStream);
    }

    private Player getPlayer(String playerNo) {
        Player player = playerRepository.findByPlayerNo(playerNo);
        if (player == null) {
            throw new ApplicationException(new ErrorResponse(
                    ErrorCodes.INTERNAL_ERROR,
                    "An error occurred while trying to get the player with player no: " + playerNo));
        }

        return player;
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Game not found with id: " + id));
    }

    public List<Game> getAllGames() {

        return gameRepository.findAll();
    }
}
