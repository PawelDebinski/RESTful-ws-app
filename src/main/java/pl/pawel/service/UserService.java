package pl.pawel.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.pawel.shared.dto.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserByUserId( String userId);
    UserDto updateUser(String id, UserDto user);
    void deleteUser(String userId);
    List<UserDto> getUsers(int page, int limit);
}
