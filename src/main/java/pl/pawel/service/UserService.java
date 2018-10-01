package pl.pawel.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.pawel.shared.dto.UserDto;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserByUserId( String userId);
}
