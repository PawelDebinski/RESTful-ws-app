package pl.pawel.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import pl.pawel.service.UserService;
import pl.pawel.shared.dto.UserDto;
import pl.pawel.ui.model.request.UserDetailsRequestModel;
import pl.pawel.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping
    public String getUser() {
        LOGGER.info("=== Inside getUser()");
        return "get user was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        LOGGER.info("=== Inside createUser()");
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserRest returnValue = new UserRest();

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser() {
        LOGGER.info("=== Inside updateUser()");
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        LOGGER.info("=== Inside deleteUser()");
        return "delete user was called";
    }
}
