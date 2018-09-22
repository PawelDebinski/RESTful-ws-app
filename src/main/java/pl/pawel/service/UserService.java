package pl.pawel.service;

import pl.pawel.shared.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);
}
