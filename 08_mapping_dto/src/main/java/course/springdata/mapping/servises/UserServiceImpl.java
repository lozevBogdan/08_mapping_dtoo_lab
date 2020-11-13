package course.springdata.mapping.servises;

import course.springdata.mapping.dto.UserDto;
import course.springdata.mapping.dto.UserLoginDto;
import course.springdata.mapping.dto.UserRegisterDto;
import course.springdata.mapping.entities.Role;
import course.springdata.mapping.entities.User;
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
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ValidatorUtil validatorUtil;
    private UserDto loggedUser;

    @Autowired
    public UserServiceImpl(ModelMapper modelMapper, UserRepository userRepository, Validator validator, GameService gameService, ValidatorUtil validatorUtil) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.gameService = gameService;
        this.validatorUtil = validatorUtil;
    }

    @Override
    public String registerUser(UserRegisterDto userRegisterDto) {
        StringBuilder sb = new StringBuilder();
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())){
            sb.append("Passwords don`t match!");
        }
        else {
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
             this.loggedUser = this.modelMapper.map(user.get(),UserDto.class);

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


}
