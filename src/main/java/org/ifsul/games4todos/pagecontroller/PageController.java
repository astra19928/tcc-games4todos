package org.ifsul.games4todos.pagecontroller;

import lombok.AllArgsConstructor;
import org.ifsul.games4todos.model.Comment;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.service.CommentService;
import org.ifsul.games4todos.service.ModService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class PageController {
    private final ModService modService;
    private final CommentService commentService;

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

        User user = mod.getUploader();
        if(user != null)
            uploaderName = user.getNickname();

        List<Comment> commentSection = commentService.findByModId(id);
        Collections.reverse(commentSection);
        int length = commentSection.size();

        model.addAttribute("gameName", mod.getFullTitle());
        model.addAttribute("modName", mod.getModName());
        model.addAttribute("uploaderName", uploaderName);
        model.addAttribute("developerName", mod.getNameDevelopers());
        model.addAttribute("description", mod.getDescription());
        model.addAttribute("releaseDate", mod.getReleaseDate());
        model.addAttribute("downloads", mod.getDownloads());
        model.addAttribute("fileSize", mod.formatFileSize());
        model.addAttribute("imgSrc", "/api/mods/" + id + "/image");
        model.addAttribute("downloadUrl", "/api/mods/" + id + "/download");
        model.addAttribute("authenticated", authenticated);
        model.addAttribute("commentSize", length);
        model.addAttribute("comments", commentSection);

        return "modificacao";
    }

    @GetMapping("/userPage")
    public String userRedirect(){
        return "index.html";
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam String query, Model model){

        List<Mod> mods = modService.findAllByQuery(query);

        model.addAttribute("query", query);
        model.addAttribute("mods", mods);

        return "search";
    }
}
