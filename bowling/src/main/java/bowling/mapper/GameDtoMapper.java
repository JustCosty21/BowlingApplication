package bowling.mapper;

import bowling.dto.GameDto;
import bowling.model.Game;
import org.springframework.stereotype.Component;

@Component
public class GameDtoMapper {

    public Game createGameFromDto(GameDto gameDto, Game game) {
        game.setGameName(gameDto.getGameName());
        game.setPlayersNo(gameDto.getPlayersNo());

        return game;
    }
}
