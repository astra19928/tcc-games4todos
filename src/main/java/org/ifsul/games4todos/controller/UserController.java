package org.ifsul.games4todos.controller;


import org.ifsul.games4todos.dto.ModResponseDTO;
import org.ifsul.games4todos.dto.UserDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.service.UserService;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FileSystemStorageService fileService;
    private final String fileDir = "users/";

    public UserController(UserService userService, FileSystemStorageService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@ModelAttribute UserDTO userDto){
        boolean imgValida = fileService.validateImage(userDto.image());
        if(!imgValida)
            return ResponseEntity.badRequest().build();

        User user = new User(null, userDto.nickname(), userDto.email(), userDto.password());
        User savedUser = userService.save(user);

        try{
            String fileDir = "users/" + savedUser.getId();
            fileService.store(userDto.image(), fileDir, "icon");
        }catch(Exception e){
            userService.delete(savedUser);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok("deez");
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getModIcon(@PathVariable("id") String userId){
        Integer id;
        try {
            id = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> foundUser = userService.findById(id);
        if (foundUser.isEmpty())
            return ResponseEntity.notFound().build();

        Resource image;
        try {
            image = fileService.loadAsResourceNoExtension( fileDir + id.toString(), "icon");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        if (!image.exists() || !image.isReadable())
            return ResponseEntity.notFound().build();

        MediaType type = MediaTypeFactory.getMediaType(image)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(type)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).cachePublic()) // MUDAR PARA HOUR DEPOIS
                .body(image);
    }

}
