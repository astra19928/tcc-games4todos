package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.model.*;
import org.ifsul.games4todos.repository.*;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.ifsul.games4todos.storage.FileSystemStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModService {
    private final ModRepository modRepository;
    private final FileSystemStorageService fileService;
    private final LocalizationTypeRepository localizationTypeRepository;
    private final ModLocalizationRepository modLocalizationRepository;
    private final ModDisabilityRepository modDisabilityRepository;
    private final DisabilityTypeRepository disabilityTypeRepository;
    private final String fileDir = "mods/";

    public Mod mapToEntity(ModDTO modDTO, User user){
        long fileSize = modDTO.file().getSize();
        float mb = (float) fileSize / (1024f * 1024f);

        return new Mod(
                null,
                user,
                modDTO.gameName(),
                modDTO.developers(),
                modDTO.modName(),
                modDTO.description(),
                0,
                modDTO.releaseDate(),
                mb
        );
    }

    @Transactional
    public String upload(ModDTO dto, Authentication auth){
        G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();
        if(details == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado!");

        User user = details.getUser();

        boolean imgValida = fileService.validateImage(dto.image());
        if(!imgValida)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem inválida!");

        Mod convertedMod = mapToEntity(dto, user);
        Mod newMod = save(convertedMod);
        Integer id = newMod.getId();

        List<Integer> localizationIds = Optional.ofNullable(dto.localizationTypeIds()).orElse(List.of());

        List<LocalizationType> localizationTypes = localizationTypeRepository.findAllById(localizationIds);

        if (localizationTypes.size() != new HashSet<>(localizationIds).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de Localização desconhecido");
        }

        List<Integer> disabilityIds = Optional.ofNullable(dto.disabilityTypeIds()).orElse(List.of());

        List<DisabilityType> disabilityTypes = disabilityTypeRepository.findAllById(disabilityIds);

        if (disabilityTypes.size() != new HashSet<>(disabilityIds).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de deficiência desconhecido");
        }
        long distinctCategories = disabilityTypes.stream()
                .map(t -> t.getCategory().getId()).distinct().count();
        if (distinctCategories != disabilityTypes.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas um tipo de deficiência por categoria");
        }

        localizationTypes.forEach(t -> modLocalizationRepository.save(
                new ModLocalization(id, t.getId())));

        disabilityTypes.forEach(t -> modDisabilityRepository.save(
                new ModDisability(id, t.getId())));

        try{
            fileService.store(dto.file(), fileDir + id, "content");
            fileService.store(dto.image(), fileDir + id, "icon");
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao tentar armazenar mod!");
        }

        return "Mod criado com sucesso!";
    }

    public Mod save(Mod mod){
        return this.modRepository.save(mod);
    }

    public void delete(Mod mod){
        this.modRepository.delete(mod);
    }

    public Optional<Mod> findById(Integer id){
        return this.modRepository.findById(id);
    }

    public List<Mod> findAllByQuery(String query){
        return this.modRepository.findAllByQuery(query);
    }
}
