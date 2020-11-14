package course.springdata.mapping.servises;

import course.springdata.mapping.dto.UserDto;
import course.springdata.mapping.dto.UserLoginDto;
import course.springdata.mapping.dto.UserRegisterDto;
import course.springdata.mapping.entities.Game;
import course.springdata.mapping.entities.Role;
import course.springdata.mapping.entities.User;
import course.springdata.mapping.repository.GameRepository;
import course.springdata.mapping.repository.UserRepository;
import course.springdata.mapping.utils.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final GameService gameService;
    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ValidatorUtil validatorUtil;
    private UserDto loggedUser;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, Validator validator, GameService gameService, GameRepository gameRepository, ValidatorUtil validatorUtil) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public String registerUser(UserRegisterDto userRegisterDto) {
        StringBuilder sb = new StringBuilder();

        if (this.userRepository.existsByEmail(userRegisterDto.getEmail())){

            sb.append("Already have a user with this email!");
        }else {


            if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
                sb.append("Passwords don`t match!");
            } else {
                if (this.validatorUtil.isValid(userRegisterDto)) {
                    User user = this.modelMapper.map(userRegisterDto, User.class);
                    if (this.userRepository.count() == 0) {
                        user.setRoles(Role.ADMIN);
                    } else {
                        user.setRoles(Role.USER);
                    }
                    sb.append(String.format("%s was created", userRegisterDto.getFullName()));
                    this.userRepository.saveAndFlush(user);
                } else {
                    for (ConstraintViolation<UserRegisterDto> e : this.validatorUtil
                            .violations(userRegisterDto)) {
                        sb.append(String.format("%s%n", e.getMessage()));
                    }

                }
            }
        }

        return sb.toString().trim();

    }

    @Override
    public String loginUser(UserLoginDto userLoginDto) {
        StringBuilder stringBuilder = new StringBuilder();
        Optional<User> user = this.userRepository
                .findByEmailAndPassword(userLoginDto.getEmail(),
                        userLoginDto.getPassword());

        if (user.isPresent()){
            if (loggedUser != null){
                stringBuilder.append("User is already logged in.");
            }else {
             this.loggedUser =
                     this.modelMapper.map(user.get(),UserDto.class);

                this.gameService.setLoggedUser(this.loggedUser);

            stringBuilder.append(String.format("Successfully logged in %s",
                    user.get().getFullName()));


            }
        }else {
            stringBuilder.append("Incorrect password/email!");
        }
        return stringBuilder.toString();
    }

    public String logoutUser(){
        StringBuilder stringBuilder = new StringBuilder();

        if (this.loggedUser != null){
            String name = this.loggedUser.getFullName();
            this.loggedUser = null;
            stringBuilder.append(
                    String.format("User %s was successfully logged out.",
                            name));
        } else {
            stringBuilder.append("Cannot log out. No user was logged in.");
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean isExistingEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public String buyGame(Long gameId) {

        StringBuilder sb = new StringBuilder();

        if (this.loggedUser == null){
            sb.append("First you should be log in, before buy game!");
        }else{

            Optional<Game> game = this.gameRepository.findById(gameId);

            if (!game.isPresent()){
                sb.append("Invalid game id!");
            }else {
                User user = this.userRepository.findByEmail(loggedUser.getEmail());
                user.getGames().add(game.get());
                this.userRepository.saveAndFlush(user);

                sb.append(String.format("The game %s is successfully added to user %s%n",
                        game.get().getTitle(),user.getFullName()));
            }
        }
        return sb.toString();
    }


    @Override
    public void printOwnedGames() {

        if (this.loggedUser == null){
            System.out.println("You should be log in ");
        }else {
            this.userRepository.findByEmail(this.loggedUser.getEmail())
                    .getGames()
                    .forEach(g-> System.out.println(g.getTitle()));
        }

    }
}
