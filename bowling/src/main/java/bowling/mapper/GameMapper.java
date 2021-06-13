package bowling.mapper;

import bowling.dto.GameDto;
import bowling.model.Game;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    private ModelMapper modelMapper;

    public GameMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GameDto convertToDto(Game game) {
        TypeMap<Game, GameDto> gameGameDtoTypeMap = modelMapper.typeMap(Game.class, GameDto.class);

        return gameGameDtoTypeMap.map(game);
    }
}
