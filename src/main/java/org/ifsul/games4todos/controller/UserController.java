package org.ifsul.games4todos.controller;


import org.ifsul.games4todos.dto.ModResponseDTO;
import org.ifsul.games4todos.dto.UserDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.service.UserService;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FileSystemStorageService fileService;
    private final String fileDir = "users/";
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, FileSystemStorageService fileService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.fileService = fileService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@ModelAttribute UserDTO userDto){
        boolean imgValida = fileService.validateImage(userDto.image());
        if(!imgValida)
            return ResponseEntity.badRequest().build();

        User user = new User(null, userDto.nickname(), userDto.email(), userDto.password());
        User savedUser = userService.save(user);

        try{
            String newFileDir = fileDir + savedUser.getId();
            fileService.store(userDto.image(), newFileDir, "icon");
        }catch(Exception e){
            userService.delete(savedUser);
            return ResponseEntity.internalServerError().build();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDto.email(), userDto.password());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/index.html")).build();
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
            image = fileService.loadAsResourceNoExtension( fileDir + id, "icon");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        if (!image.exists() || !image.isReadable())
            return ResponseEntity.notFound().build();

        MediaType type = MediaTypeFactory.getMediaType(image)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(type)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic()) // MUDAR PARA HOUR DEPOIS
                .body(image);
    }

}
