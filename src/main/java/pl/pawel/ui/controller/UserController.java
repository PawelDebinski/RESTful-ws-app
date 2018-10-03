package pl.pawel.ui.controller;

import org.modelmapper.ModelMapper;
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
import pl.pawel.ui.model.response.*;

import java.util.ArrayList;
import java.util.List;

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
                  produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        LOGGER.info("=== Inside createUser()");

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper(); // need to add dependency in pom
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        LOGGER.info("===  userDto from request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        LOGGER.info("===  userDto from database: {}", createdUser);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(createdUser, returnValue);
        LOGGER.info("===  userRest: {}", returnValue);

        return returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser (@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        LOGGER.info("=== Inside updateUser()");

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);
        LOGGER.info("===  userDto from request: {}", userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        LOGGER.info("===  userDto from database: {}", updatedUser);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(updatedUser, returnValue);
        LOGGER.info("===  userRest: {}", returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}",
                   produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        LOGGER.info("=== Inside deleteUser()");
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue ="0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {
        LOGGER.info("=== Inside getUsers()");

        List<UserDto> users = userService.getUsers(page, limit);

        List<UserRest> returnValue = new ArrayList<>();
        for(UserDto userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }
}
