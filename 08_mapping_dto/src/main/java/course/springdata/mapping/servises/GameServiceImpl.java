package course.springdata.mapping.servises;
import course.springdata.mapping.dto.AddGameDto;
import course.springdata.mapping.dto.DeleteGameDto;
import course.springdata.mapping.dto.UserDto;
import course.springdata.mapping.entities.Game;
import course.springdata.mapping.entities.Role;
import course.springdata.mapping.repository.GameRepository;
import course.springdata.mapping.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final ModelMapper modelMapper;
    private final GameRepository gameRepository;
    private final ValidatorUtil validatorUtil;
    private UserDto userDto;

    @Autowired
    public GameServiceImpl(ModelMapper modelMapper
            , GameRepository gameRepository, ValidatorUtil validatorUtil) {
        this.modelMapper = modelMapper;
        this.gameRepository = gameRepository;
        this.validatorUtil = validatorUtil;
    }
    @Override
    public String addGame(AddGameDto addGameDto) {
        StringBuilder sb = new StringBuilder();

        if (this.gameRepository.existsByTitle(addGameDto.getTitle())){
            sb.append("Already exist game with this title!");

        }else {
            if (this.userDto == null){
                sb.append("Invalid logged in user.");
            }
            else if((this.userDto.getRole() == Role.USER)) {
                sb.append("Invalid logged in user,");

            } else if  (this.validatorUtil.isValid(addGameDto)){

                Game game = this.modelMapper.map(addGameDto,Game.class);
                this.gameRepository.saveAndFlush(game);
                sb.append(String.format("Added %s ",game.getTitle()));

            }else {
                this.validatorUtil.violations(addGameDto)
                        .forEach(e-> sb.append(e.getMessage())
                                .append(System.lineSeparator()));
            }
        }
        return sb.toString();
    }

    @Override
    public void setLoggedUser(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public String deleteGame(DeleteGameDto deleteGameDto) {

        StringBuilder sb = new StringBuilder();
        if((this.userDto == null) || (this.userDto.getRole() == Role.USER)) {
            sb.append("Invalid logged in user,");

        }else {

            Optional<Game> game =
                    this.gameRepository.findById(deleteGameDto.getId());
            if(game.isPresent()){
                String name = game.get().getTitle();
                this.gameRepository.delete(game.get());
                sb.append(String.format("Game %s was deleted",
                        name));
            }else{
                sb.append("Can not find game.");
            }
        }
        return sb.toString();
    }

    public String editGame(Long id, List<String> values) {

        StringBuilder sb =  new StringBuilder();

        if((this.userDto == null)){
            sb.append("Invalid logged in user.");

        }else if (this.userDto.getRole() == Role.USER) {
            sb.append("Invalid logged in user,");

        }else {

            if (this.gameRepository.findById(id).isEmpty()) {

                sb.append("Invalid game id!");
            } else {

                Game game = this.gameRepository.findById(id).get();

                for (int i = 0; i < values.size(); i++) {

                    String[] tokens = values.get(i).split("=");
                    String command = tokens[0];

                    switch (command) {
                        case "price":
                            BigDecimal price = new BigDecimal(tokens[1]);
                            game.setPrice(price);
                            break;
                        case "size":
                            double size = Double.parseDouble(tokens[1]);
                            game.setSize(size);
                            break;
                        case "title":
                            String title = tokens[1];
                            game.setTitle(title);
                            break;
                        case "trailer":
                            game.setTrailer(tokens[1]);
                            break;
                        case "imageThumbnail":
                            game.setImageThumbnail(tokens[1]);
                            break;
                        case "description":
                            game.setDescription(tokens[1]);
                            break;
                    }
                }
                this.gameRepository.saveAndFlush(game);
                sb.append(String.format("Edited %s", game.getTitle()));

            }
        }
        return sb.toString();
    }

    @Override
    public boolean isExistTitle(String title) {
        return this.gameRepository.existsByTitle(title);
    }

    @Override
    public void printAllGame() {
        this.gameRepository.findAll()
                .forEach(g-> System.out.printf
                        ("%s %s",g.getTitle(),g.getPrice()));
    }

    @Override
    public void getDetailAbout(String titleGame) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("\"dd-MM-yyyy\"");
        Game game = this.gameRepository.findByTitle(titleGame);
        String date = formatter.format(game.getRealiseDate());
                System.out.printf("Title: %s%n",game.getTitle());
        System.out.printf("Price: %s%n",game.getPrice());
        System.out.printf("Description: %s%n",game.getDescription());
        System.out.printf("Release date: %s %n",date);

    }
}
