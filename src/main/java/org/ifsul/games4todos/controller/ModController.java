package org.ifsul.games4todos.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.ModService;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.io.File;

@RestController
@RequestMapping("/mod")
public class ModController {

    private final ModService modService;
    private final FileSystemStorageService fileService;

    public ModController(ModService modService, FileSystemStorageService fileService) {
        this.modService = modService;
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> gehehehaThankYou(
            @ModelAttribute ModDTO mod,
            Authentication auth
    ){
        G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();
        User user = details.getUser();

        if(user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Integer userID = user.getId();

        boolean imgValida = modService.validateImage(mod.getImage());
        if(!imgValida)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mod convertedMod = modService.convertUnverified(mod, userID);
        Mod newMod = modService.save(convertedMod);

        try{
            String fileDir = String.valueOf(newMod.getId());
            fileService.store(mod.getFile(), fileDir, "content");
            fileService.store(mod.getImage(), fileDir, "icon");
            //fileService.store(mod.getPermissionImage(), fileDir);

        }catch(Exception e){
            modService.delete(convertedMod);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok("O mod foi enviado com sucesso e está sendo verificado e processado");
    }
}
