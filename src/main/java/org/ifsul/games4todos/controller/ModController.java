package org.ifsul.games4todos.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.ModService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController
@RequestMapping("/mod")
public class ModController {

    private final ModService modService;

    public ModController(ModService modService) {
        this.modService = modService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> gehehehaThankYou(
            @ModelAttribute ModDTO mod,
            Authentication auth
    ){
        G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();
        User user = details.getUser();
        Integer userID = user.getId();

        Mod convertedMod = modService.convertUnverified(mod, userID);
        modService.save(convertedMod);

        //store image and mod file somewhere around here

        // if BADBADNOTGOOD HAPPENS
        if(false){
            modService.delete(convertedMod);
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        
        return ResponseEntity.ok("O mod foi enviado com sucesso e está sendo verificado e processado");
    }
}
