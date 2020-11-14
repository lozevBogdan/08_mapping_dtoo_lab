package course.springdata.mapping.servises;

import course.springdata.mapping.dto.UserLoginDto;
import course.springdata.mapping.dto.UserRegisterDto;

public interface UserService {

   String registerUser(UserRegisterDto userRegisterDto );

   String loginUser(UserLoginDto userLoginDto);
   String logoutUser();
   boolean isExistingEmail(String email);
   String buyGame(Long gameId);

   void printOwnedGames();
}
