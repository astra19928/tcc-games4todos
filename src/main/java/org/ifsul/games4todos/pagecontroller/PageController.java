package org.ifsul.games4todos.pagecontroller;

import lombok.AllArgsConstructor;
import org.ifsul.games4todos.mapper.ModMapper;
import org.ifsul.games4todos.model.*;
import org.ifsul.games4todos.repository.*;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
    private final LocalizationTypeRepository localizationTypeRepository;
    private final DisabilityCategoryRepository disabilityCategoryRepository;
    private final ModDisabilityRepository modDisabilityRepository;
    private final ModLocalizationRepository modLocalizationRepository;
    private final FavoriteRepository favoriteRepository;
    private final RecommendationService recommendationService;

    @GetMapping("/mods/{id}")
    public String modPage(@PathVariable("id") Integer id, Model model, Authentication auth) {

        Optional<Mod> foundMod = modService.findById(id);
        if (foundMod.isEmpty()){
            model.addAttribute("responseMessage", "Mod não encontrado");
            return "modificacao";
        }

        Mod mod = foundMod.get();

        String uploaderName = "Usuário deletado";

        User uploader = mod.getUploader();
        if(uploader != null)
            uploaderName = uploader.getNickname();

        boolean authenticated = auth != null && auth.isAuthenticated();
        if(authenticated){
            G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();
            User user = details.getUser();
            if(uploader != null && user.getId().equals(uploader.getId()))
                model.addAttribute("isUploader", true);

            Optional<Favorite> favorite = favoriteRepository.findById_IdUserAndId_IdMod(user.getId(), id);
            boolean isFavorited = favorite.isPresent();

            model.addAttribute("userId", user.getId());
            model.addAttribute("isFavorite", isFavorited);
            model.addAttribute("userIcon", user.getIconSrc());
        }

        List<Comment> commentSection = commentService.findByModId(id);
        Collections.reverse(commentSection);
        int length = commentSection.size();

        List<ModLocalization> modLocalizations = localizationService.getAllTypesByModId(id);
        List<LocalizationType> localizationTypes = modLocalizations.stream()
                .map(ModLocalization::getType)
                .toList();

        List<ModDisability> modDisabilities = disabilityService.findAllByModId(id);
        List<DisabilityType> disabilityTypes = modDisabilities.stream()
                        .map(ModDisability::getType)
                        .toList();

        model.addAttribute("id", id);
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
        model.addAttribute("disabilityTypes", disabilityTypes);
        model.addAttribute("localizationTypes", localizationTypes);

        return "modificacao";
    }

    @GetMapping("/")
    public String index(Model model, Authentication auth) {
        User user = null;
        if (auth != null && auth.getPrincipal() instanceof G4TUserDetails details)
            user = details.getUser();

        model.addAttribute("recommendations", recommendationService.recommend(user));
        return "index";
    }

    @GetMapping("/mods/{id}/config")
    public String modConfig(@PathVariable Integer id, Model model, Authentication auth) {
        G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();

        Mod mod = modService.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Mod não encontrado!"));

        // Only the uploader can open the config page
        if (mod.getUploader() == null
                || !mod.getUploader().getId().equals(details.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não é o autor deste mod!");

        model.addAttribute("mod", mod);

        // Same data the upload page uses for the dropdowns
        model.addAttribute("localizations", localizationTypeRepository.findAll());
        model.addAttribute("disabilities", disabilityCategoryRepository.findAll());

        // Ids already saved for this mod, used to pre-select the chips
        model.addAttribute("selectedLocalizationIds",
                modLocalizationRepository.findTypeIdsByModId(id));
        model.addAttribute("selectedDisabilityIds",
                modDisabilityRepository.findTypeIdsByModId(id));

        return "mod-config";
    }

    @GetMapping("/userPage")
    public String userRedirect(){
        return "/";
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

    @GetMapping("/favorites")
    public String favorites(Model model, Authentication auth){
        if(auth.getPrincipal() == null){
            model.addAttribute("responseMessage", "Não pode visualizar favoritos sem se autenticar!");
            return "user/favorites";
        }

        List<Mod> mods = modService.getFavoritados(auth);

        mods.forEach((mod) -> {
            String newDesc = ModMapper.shortenDescription(mod.getDescription());

            mod.setDescription(newDesc);
        });

        model.addAttribute("mods", mods);

        return "user/favorites";
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
