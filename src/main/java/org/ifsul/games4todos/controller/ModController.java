package org.ifsul.games4todos.controller;

import org.ifsul.games4todos.dto.ModDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mod")
public class ModController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> gehehehaThankYou(
            @ModelAttribute ModDTO mod
    ){
        return ResponseEntity.ok("hehehee you did sooo well im so proud of you <3");
    }
}
