package course.springdata.mapping.servises;

import course.springdata.mapping.dto.AddGameDto;
import course.springdata.mapping.dto.DeleteGameDto;
import course.springdata.mapping.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface GameService {


    String addGame(AddGameDto addGameDto);

    void setLoggedUser(UserDto userDto);

    String deleteGame(DeleteGameDto deleteGameDto);

}
