package pl.pawel.ui.controller;

import org.springframework.web.bind.annotation.*;
import pl.pawel.ui.model.request.UserDetailsRequestModel;
import pl.pawel.ui.model.response.UserRest;

@RestController
@RequestMapping("users")
public class UserController {

    @GetMapping
    public String getUser() {
        return "get user was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        return null;
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
