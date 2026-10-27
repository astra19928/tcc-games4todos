package org.ifsul.games4todos.pagecontroller;

import lombok.AllArgsConstructor;
import org.ifsul.games4todos.dto.ModResponseDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.ModService;
import org.ifsul.games4todos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@AllArgsConstructor
@Controller
public class ModPageController {
    private final ModService modService;
    private final UserService userService;

    @GetMapping("/mods/{id}")
    public String modPage(@PathVariable("id") String modId, Model model, Authentication auth) {
        Integer id;
        try {
            id = Integer.parseInt(modId);
        } catch (NumberFormatException e) {
            model.addAttribute("responseMessage", "Apenas IDs numéricos permitidos");
            return "modificacao";
        }

        Optional<Mod> foundMod = modService.findById(id);
        if (foundMod.isEmpty()){
            model.addAttribute("responseMessage", "Mod não encontrado");
            return "modificacao";
        }

        Mod mod = foundMod.get();

        String uploaderName = "Usuário deletado";

        boolean authenticated = auth != null && auth.isAuthenticated();

        Optional<User> foundUploader = userService.findById(mod.getIdUploader());
        if(foundUploader.isPresent())
            uploaderName = foundUploader.get().getNickname();

        String formatFile = String.format("%.2f", mod.getFileSize());
        model.addAttribute("gameName", mod.getGameName() + " (" + mod.getModName() + ")");
        model.addAttribute("modName", mod.getModName());
        model.addAttribute("uploaderName", uploaderName);
        model.addAttribute("developerName", mod.getNameDevelopers());
        model.addAttribute("description", mod.getDescription());
        model.addAttribute("releaseDate", mod.getReleaseDate());
        model.addAttribute("downloads", mod.getDownloads());
        model.addAttribute("fileSize", formatFile);
        model.addAttribute("imgSrc", "/api/mods/" + id + "/image");
        model.addAttribute("downloadUrl", "/api/mods/" + id + "/download");
        model.addAttribute("authenticated", authenticated);

        return "modificacao";
    }
}
