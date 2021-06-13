package bowling.mapper;

import bowling.dto.PlayerDto;
import bowling.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerDtoMapper {

    public Player createPlayerFromDto(PlayerDto playerDto, Player player) {
        player.setFirstName(playerDto.getFirstName());
        player.setLastName(playerDto.getLastName());
        player.setPlayerNo(playerDto.getPlayerNo());
        player.setFrames(playerDto.getFrames());

        return player;
    }
}
