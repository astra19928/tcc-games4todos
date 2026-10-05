package org.ifsul.games4todos.controller;


import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User cadastrar(@RequestBody User user){
        return userService.salvar(user);
    }

}
