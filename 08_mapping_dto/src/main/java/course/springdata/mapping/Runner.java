package course.springdata.mapping;

import course.springdata.mapping.dto.AddGameDto;
import course.springdata.mapping.dto.DeleteGameDto;
import course.springdata.mapping.dto.UserLoginDto;
import course.springdata.mapping.dto.UserRegisterDto;
import course.springdata.mapping.servises.GameService;
import course.springdata.mapping.servises.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {

    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public Runner(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to my homework 'Spring Data Auto Mapping Objects'!");
        System.out.println("Please enter your input or 'Stop' for exist:");

        String input;

        while (!((input =reader.readLine()).equalsIgnoreCase("stop"))) {

            String[] tokens = input.split("\\|");

            switch (tokens[0]) {

                case "RegisterUser":
                    UserRegisterDto user =
                            new UserRegisterDto
                                    (tokens[1], tokens[2], tokens[3], tokens[4]);

                    System.out.println(this.userService.registerUser(user));
                    break;
                case "LoginUser":
                    UserLoginDto userLoginDto = new UserLoginDto(tokens[1],tokens[2]);

                    System.out.println(this.userService.loginUser(userLoginDto));
                    break;

                case "Logout":
                    System.out.println(this.userService.logoutUser());
                    break;
                case "AddGame":
                    LocalDate date = LocalDate.parse(tokens[7],
                            DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    AddGameDto addGameDto = new AddGameDto(tokens[1],
                            new BigDecimal(tokens[2]),Double.parseDouble(tokens[3]),
                            tokens[4],tokens[5],tokens[6],date);

                    System.out.println(this.gameService.addGame(addGameDto));
                    break;

                case "EditGame":
                    Long id = Long.parseLong(tokens[1]);
                List<String> values = new ArrayList<>();

                    for (int i = 2; i <tokens.length ; i++) {
                        values.add(tokens[i]);
                    }
                    System.out.println(this.gameService.editGame(id,
                            (values)));
                    break;

                case "DeleteGame":
                    DeleteGameDto deleteGameDto = new DeleteGameDto(
                           Long.parseLong(tokens[1]));
                    System.out.println(this.gameService.deleteGame(deleteGameDto));
                    break;

                case "AllGames":
                this.gameService.printAllGame();
                    break;
                case "DetailGame":
                this.gameService.getDetailAbout(tokens[1]);
                    break;

                case "buyGame":
                    System.out.println(this.userService.buyGame
                            (Long.parseLong(tokens[1])));
                    break;

                case "OwnedGames":
                    this.userService.printOwnedGames();
                    break;
                default:
                    System.out.println("Incorrect input, please try again:");
                    break;

            }

            System.out.println("Please enter your input or 'Stop' for exist:");
        }


    }
}
