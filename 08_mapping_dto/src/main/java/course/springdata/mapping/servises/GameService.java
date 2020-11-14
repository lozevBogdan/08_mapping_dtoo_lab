package course.springdata.mapping.servises;

import course.springdata.mapping.dto.AddGameDto;
import course.springdata.mapping.dto.DeleteGameDto;
import course.springdata.mapping.dto.UserDto;
import org.springframework.stereotype.Service;
import java.util.List;

import java.awt.*;

@Service
public interface GameService {


    String addGame(AddGameDto addGameDto);

    void setLoggedUser(UserDto userDto);

    String deleteGame(DeleteGameDto deleteGameDto);

    String editGame(Long id, List<String> values);

    boolean isExistTitle(String title);

    void printAllGame();

    void getDetailAbout(String titleGame);

}
