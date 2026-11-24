package org.ifsul.games4todos.controller;

import lombok.AllArgsConstructor;
import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.dto.ModResponseDTO;
import org.ifsul.games4todos.model.Comment;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.CommentService;
import org.ifsul.games4todos.service.ModService;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.jspecify.annotations.NonNull;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
@AllArgsConstructor
@RestController
@RequestMapping("/mods")
public class ModController {

    private final ModService modService;
    private final FileSystemStorageService fileService;
    private final CommentService commentService;
    private final String fileDir = "mods/";


    @PostMapping(path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> receiveModUpload(
            @ModelAttribute ModDTO mod,
            Authentication auth
    ){
        return ResponseEntity.ok(modService.upload(mod, auth));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModResponseDTO> viewModById(@PathVariable("id") String modId){
        Integer id;
        try{
            id = Integer.parseInt(modId);
        }catch(NumberFormatException e){
            return ResponseEntity.badRequest().build();
        }

        Optional<Mod> foundMod = modService.findById(id);
        if(foundMod.isEmpty())
            return ResponseEntity.notFound().build();

        ModResponseDTO responseDTO = getModResponseDTO(foundMod.get());
        return ResponseEntity.ok(responseDTO);
    }

    private static @NonNull ModResponseDTO getModResponseDTO(Mod mod) {

        String uploaderName = "Usuário deletado";

        User uploader = mod.getUploader();
        if(uploader != null)
            uploaderName = uploader.getNickname();

        return new ModResponseDTO(
                mod.getGameName(),
                mod.getModName(),
                uploaderName,
                mod.getNameDevelopers(),
                mod.getDescription(),
                mod.getReleaseDate(),
                mod.getDownloads(),
                mod.getFileSize()
        );
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getModIcon(@PathVariable("id") Integer id){
        Optional<Mod> foundMod = modService.findById(id);
        if (foundMod.isEmpty())
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
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic())
                .body(image);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadMod(@PathVariable("id") Integer id){
        Optional<Mod> foundMod = modService.findById(id);
        if (foundMod.isEmpty())
            return ResponseEntity.notFound().build();

        Resource file;
        try {
            file = fileService.loadAsResourceNoExtension(fileDir + id, "content");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        if (!file.exists() || !file.isReadable())
            return ResponseEntity.notFound().build();

        Mod mod = foundMod.get();
        int newDownloads = mod.getDownloads() + 1;

        mod.setDownloads(newDownloads);
        modService.save(mod);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.getFilename())
                                .build()
                                .toString())
                .body(file);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<String> sendComment(@PathVariable("id") Integer id, @RequestBody String content, Authentication auth){
        Optional<Mod> foundMod = modService.findById(id);
        if (foundMod.isEmpty())
            return ResponseEntity.notFound().build();

        Mod mod = foundMod.get();
        if(content.length() > 300)
            return ResponseEntity.badRequest().build();

        G4TUserDetails g4tUserDetails = (G4TUserDetails) auth.getPrincipal();
        if(g4tUserDetails == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = g4tUserDetails.getUser();

        Comment comment = commentService.createComment(content, user, mod);

        if(comment == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok("Upload de comentário feito com sucesso.");
    }

}
