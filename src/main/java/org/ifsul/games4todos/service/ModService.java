package org.ifsul.games4todos.service;

import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.repository.ModRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModService {
    private final ModRepository modRepository;

    public ModService(ModRepository modRepository) {
        this.modRepository = modRepository;
    }

    public Mod convertUnverified(ModDTO modDTO, Integer userID){
        long fileSize = modDTO.getFile().getSize();
        float mb = (float) fileSize / (1024f * 1024f);

        Mod mod = new Mod(
                null,
                userID,
                modDTO.getGameName(),
                modDTO.getDevelopers(),
                modDTO.getModName(),
                0,
                modDTO.getReleaseDate(),
                mb,
                false
        );

        return mod;
    }

    public Mod save(Mod mod){return this.modRepository.save(mod);}

    @Async
    public Mod processMod(Mod mod){
        //does nothing for now. should make virustotal api call here
        return null;
    }



}
