package org.ifsul.games4todos.controller;

import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.dto.ModResponseDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.ModService;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
    public ResponseEntity<?> receiveModUpload(
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

        Mod mod = foundMod.get();
        File img = fileService.load(id.toString()).toFile();

        ModResponseDTO responseDTO = new ModResponseDTO(
                mod.getGameName(),
                mod.getModName(),
                mod.getNameDevelopers(),
                "placeholder",
                mod.getDownloads(),
                mod.getReleaseDate(),
                mod.getFileSize()
        );
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getModIcon(@PathVariable("id") String modId){
        Integer id;
        try {
            id = Integer.parseInt(modId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Mod> foundMod = modService.findById(id);
        if (foundMod.isEmpty())
            return ResponseEntity.notFound().build();

        Resource image;
        try {
            image = fileService.loadAsResourceNoExtension(id.toString(), "icon");
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
    public ResponseEntity<Resource> downloadMod(@PathVariable("id") String modId){
        Integer id;
        try {
            id = Integer.parseInt(modId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Mod> foundMod = modService.findById(id);
        if (foundMod.isEmpty())
            return ResponseEntity.notFound().build();

        Resource file;
        try {
            file = fileService.loadAsResourceNoExtension(id.toString(), "content");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        if (!file.exists() || !file.isReadable())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.getFilename())
                                .build()
                                .toString())
                .body(file);
    }

}
