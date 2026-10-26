package org.ifsul.games4todos.controller;

import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.dto.ModResponseDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.ModService;
import org.ifsul.games4todos.service.UserService;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/mods")
public class ModController {

    private final ModService modService;
    private final UserService userService;
    private final FileSystemStorageService fileService;
    private final String fileDir = "mods/";

    public ModController(ModService modService, UserService userService, FileSystemStorageService fileService) {
        this.modService = modService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping(path = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> receiveModUpload(
            @ModelAttribute ModDTO mod,
            Authentication auth
    ){
        G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();
        if(details == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = details.getUser();

        Integer userID = user.getId();

        boolean imgValida = fileService.validateImage(mod.image());
        if(!imgValida)
            return ResponseEntity.badRequest().build();

        Mod convertedMod = modService.convertUnverified(mod, userID);
        Mod newMod = modService.save(convertedMod);

        try{
            fileService.store(mod.file(), fileDir, "content");
            fileService.store(mod.image(), fileDir, "icon");
            //fileService.store(mod.getPermissionImage(), fileDir);

        }catch(Exception e){
            modService.delete(convertedMod);
            return ResponseEntity.internalServerError().build();
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

        String uploaderName = "Usuário deletado";

        Optional<User> foundUploader = userService.findById(mod.getIdUploader());
        if(foundUploader.isPresent())
            uploaderName = foundUploader.get().getNickname();

        ModResponseDTO responseDTO = new ModResponseDTO(
                mod.getGameName(),
                mod.getModName(),
                uploaderName,
                mod.getNameDevelopers(),
                mod.getDescription(),
                mod.getReleaseDate(),
                mod.getDownloads(),
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
            file = fileService.loadAsResourceNoExtension(fileDir + id.toString(), "content");
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

}
