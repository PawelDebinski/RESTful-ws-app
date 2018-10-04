package pl.pawel.ui.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.pawel.exceptions.UserServiceException;
import pl.pawel.service.AddressService;
import pl.pawel.service.UserService;
import pl.pawel.shared.dto.AddressDto;
import pl.pawel.shared.dto.UserDto;
import pl.pawel.ui.model.request.UserDetailsRequestModel;
import pl.pawel.ui.model.response.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;


    @GetMapping(path = "/{id}",
                produces ={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        LOGGER.info("=== Inside getUser()");
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserRest.class);

        return returnValue;
    }

    @PostMapping( consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
                  produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        LOGGER.info("=== Inside createUser()");

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        //Bean utils nadaje się do kopiowania prostych POJO, przy arrayach potrafi buggować, dlatego lepiej dodać dependency do Model Mappera
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails, userDto);
        ModelMapper modelMapper = new ModelMapper(); // need to add dependency in pom
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

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

        UserDto updatedUser = userService.updateUser(id, userDto);

        UserRest returnValue = new UserRest();
        BeanUtils.copyProperties(updatedUser, returnValue);

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
        Type listType = new TypeToken<List<UserRest>>() {}.getType();
        returnValue = new ModelMapper().map(users, listType);

        return returnValue;
    }

    // http://localhost:8080/users/fj3498tywe9/addresses
    @GetMapping(path = "/{id}/addresses",
                produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<AddressesRest> getUserAddresses(@PathVariable String id){
        LOGGER.info("=== Inside getUserAddresses()");
        List<AddressDto> addressesDto = addressService.getAddresses(id);

        List<AddressesRest> returnValue = new ArrayList<>();
        if(addressesDto != null && !addressesDto.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            returnValue = new ModelMapper().map(addressesDto, listType);
        }

        return returnValue;
    }

    @GetMapping(path = "/{id}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public AddressesRest getUserAddress(@PathVariable String addressId) {
        LOGGER.info("=== Inside getUserAddress()");
        AddressDto addressDto = addressService.getAddress(addressId);
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map(addressDto, AddressesRest.class);
    }
}
