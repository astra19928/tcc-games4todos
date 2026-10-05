package org.ifsul.games4todos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class babebarr {
    @GetMapping("/ogumoom")
    public String test() {
        return "yahahahaaahha!!";
    }
}

