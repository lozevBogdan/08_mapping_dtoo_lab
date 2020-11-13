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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        while (true) {
            String[] tokens = reader.readLine().split("\\|");

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

                case "Edit":break;

                case "DeleteGame":

                    DeleteGameDto deleteGameDto = new DeleteGameDto(
                           Long.parseLong(tokens[1]));

                    System.out.println(this.gameService.deleteGame(deleteGameDto));

                    break;

            }


        }


    }
}
