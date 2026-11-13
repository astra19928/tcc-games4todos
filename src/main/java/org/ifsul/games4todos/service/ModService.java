package org.ifsul.games4todos.service;

import org.ifsul.games4todos.dto.ModDTO;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.ModRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModService {
    @Autowired
    private final ModRepository modRepository;

    public ModService(ModRepository modRepository) {
        this.modRepository = modRepository;
    }


    public Mod convertUnverified(ModDTO modDTO, User user){
        long fileSize = modDTO.file().getSize();
        float mb = (float) fileSize / (1024f * 1024f);

        Mod mod = new Mod(
                null,
                user,
                modDTO.gameName(),
                modDTO.developers(),
                modDTO.modName(),
                modDTO.description(),
                0,
                modDTO.releaseDate(),
                mb,
                false
        );

        return mod;
    }

    public Mod save(Mod mod){
        return this.modRepository.save(mod);
    }

    public void delete(Mod mod){
        this.modRepository.delete(mod);
    }

    @Async
    public Mod processMod(Mod mod){
        //does nothing for now. should make virustotal api call here
        return null;
    }

    public Optional<Mod> findById(Integer id){
        return this.modRepository.findById(id);
    }

    public List<Mod> findAllByQuery(String query){
        return this.modRepository.findAllByQuery(query);
    }
}
