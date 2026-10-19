package org.ifsul.games4todos.pagecontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModPageController {

    @GetMapping("/mods/{id}")
    public String modPage(){
        return "forward:/modificacao.html";
    }
}
