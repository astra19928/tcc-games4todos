package org.ifsul.games4todos.pagecontroller;

import lombok.AllArgsConstructor;
import org.ifsul.games4todos.mapper.ModMapper;
import org.ifsul.games4todos.model.*;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class PageController {
    private final ModService modService;
    private final CommentService commentService;
    private final UserService userService;
    private final DisabilityService disabilityService;
    private final LocalizationService localizationService;

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
        mods.forEach((mod) -> {
            String newDesc = ModMapper.shortenDescription(mod.getDescription());

            mod.setDescription(newDesc);
        });

        model.addAttribute("query", query);
        model.addAttribute("mods", mods);

        return "search";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model){
        List<DisabilityCategory> allDisabilities = disabilityService.findAllCategories();
        model.addAttribute("disabilities", allDisabilities);

        return "cadastro";
    }

    @GetMapping("/users/me/config")
    public String userConfig(Model model, Authentication auth){
        G4TUserDetails g4tUserDetails = (G4TUserDetails) auth.getPrincipal();
        if(g4tUserDetails == null)
            return "/login.html";

        User detailsUser = g4tUserDetails.getUser();
        User user = userService.fetch(detailsUser);
        List<DisabilityType> disabilities = user.getDisabilityTypes();
        List<DisabilityCategory> allDisabilities = disabilityService.findAllCategories();

        HashMap<Integer, Integer> categoryTypeMap = new HashMap<>();

        for(DisabilityType dis : disabilities)
            categoryTypeMap.put(dis.getCategory().getId(), dis.getId());

        model.addAttribute("username", user.getNickname());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("disabilities", allDisabilities);
        model.addAttribute("userDisabilitiesMap", categoryTypeMap);
        model.addAttribute("user", user);

        return "user-config";
    }

    @GetMapping("user/upload")
    public String modUpload(Model model, Authentication auth){
        G4TUserDetails g4tUserDetails = (G4TUserDetails) auth.getPrincipal();
        if(g4tUserDetails == null)
            return "/login.html";

        System.out.println("MAPPED");

        List<LocalizationType> localizationTypes = localizationService.getAllTypes();
        List<DisabilityCategory> allDisabilities = disabilityService.findAllCategories();

        model.addAttribute("disabilities", allDisabilities);
        model.addAttribute("localizations", localizationTypes);

        return "user/upload";
    }
}
