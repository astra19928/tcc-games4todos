package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.dto.ModUpdateDTO;
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

        List<Integer> localizationIds = Optional.ofNullable(dto.localizationTypes()).orElse(List.of());

        List<LocalizationType> localizationTypes = localizationTypeRepository.findAllById(localizationIds);

        if (localizationTypes.size() != new HashSet<>(localizationIds).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de Localização desconhecido");
        }

        List<Integer> disabilityIds = Optional.ofNullable(dto.disabilityTypes()).orElse(List.of());

        List<DisabilityType> disabilityTypes = disabilityTypeRepository.findAllById(disabilityIds);

        if (disabilityTypes.size() != new HashSet<>(disabilityIds).size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de deficiência desconhecido");
        }

        modLocalizationRepository.saveAll(
                localizationTypes.stream().map(t -> new ModLocalization(newMod, t)).toList());
        modDisabilityRepository.saveAll(
                disabilityTypes.stream().map(t -> new ModDisability(newMod, t)).toList());

        try{
            fileService.store(dto.file(), fileDir + id, "content");
            fileService.store(dto.image(), fileDir + id, "icon");
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao tentar armazenar mod!");
        }

        return "Mod criado com sucesso!";
    }

    @Transactional
    public String update(Integer id, ModUpdateDTO dto, Authentication auth) {
        G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();
        Mod mod = findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Mod não encontrado!"));

        if (mod.getUploader() == null || !mod.getUploader().getId().equals(details.getUser().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não é o autor deste mod!");

        mod.setModName(dto.modName());
        mod.setNameDevelopers(dto.developers());
        mod.setDescription(dto.description());
        mod.setReleaseDate(dto.releaseDate());

        if (dto.file() != null && !dto.file().isEmpty())
            mod.setFileSize((float) dto.file().getSize() / (1024f * 1024f));

        modLocalizationRepository.deleteAllByModId(id);
        modDisabilityRepository.deleteAllByModId(id);

        if (dto.image() != null && !dto.image().isEmpty()) {
            if (!fileService.validateImage(dto.image()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem inválida!");
            fileService.store(dto.image(), fileDir + id, "icon");
        }

        if (dto.file() != null && !dto.file().isEmpty())
            fileService.store(dto.file(), fileDir + id, "content");

        return "Mod atualizado com sucesso!";
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
