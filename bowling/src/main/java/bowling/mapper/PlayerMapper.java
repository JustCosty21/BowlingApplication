package bowling.mapper;

import bowling.dto.PlayerDto;
import bowling.model.Player;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    private ModelMapper modelMapper;

    public PlayerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PlayerDto convertToDto(Player player) {
        TypeMap<Player, PlayerDto> playerplayerDtoTypeMap = modelMapper.typeMap(Player.class, PlayerDto.class);

        return playerplayerDtoTypeMap.map(player);
    }
}
