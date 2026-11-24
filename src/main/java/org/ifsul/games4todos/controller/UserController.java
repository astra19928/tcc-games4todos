package org.ifsul.games4todos.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.ifsul.games4todos.dto.UserDTO;
import org.ifsul.games4todos.dto.UserPatchDTO;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.service.UserService;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

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
    public ResponseEntity<?> cadastrar(@ModelAttribute UserDTO dto, HttpServletRequest request){
        boolean imgValida = fileService.validateImage(dto.image());
        if(!imgValida)
            return ResponseEntity.badRequest().build();


        User savedUser = userService.create(dto);


        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                dto.email(), dto.password());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        request.getSession(true)
                .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
    }

    @PatchMapping("/{id}")
    public void alterar(@ModelAttribute UserPatchDTO dto, Authentication authentication, @PathVariable Integer id){
        userService.patch(dto, authentication, id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getModIcon(@PathVariable("id") Integer id){

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
                .body(image);
    }

}
