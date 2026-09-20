package org.ifsul.games4todos;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @PostMapping("/cadastro")
    public String test(@RequestBody UserDTO userDTO) {
        return userDTO.toString();
    }
}
