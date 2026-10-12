package org.ifsul.games4todos.controller;

import org.ifsul.games4todos.model.Mod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mod")
public class ModController {

    @PostMapping
    public ResponseEntity<?> gehehehaThankYou(
            @RequestBody Mod mod
    ){
        return ResponseEntity.ok("hehehee you did sooo well im so proud of you <3");
    }
}
