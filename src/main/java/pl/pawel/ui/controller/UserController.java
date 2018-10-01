package pl.pawel.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.pawel.exceptions.UserServiceException;
import pl.pawel.service.UserService;
import pl.pawel.shared.dto.UserDto;
import pl.pawel.ui.model.request.UserDetailsRequestModel;
import pl.pawel.ui.model.response.ErrorMessages;
import pl.pawel.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}",
            produces ={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        LOGGER.info("=== Inside getUser()");
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping( consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces ={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        LOGGER.info("=== Inside createUser() -> userDetails: {}", userDetails);

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);
        LOGGER.info("===  userDto from request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        LOGGER.info("===  userDto from database: {}", createdUser);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(createdUser, returnValue);
        LOGGER.info("===  userRest: {}", returnValue);

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
