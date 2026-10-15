package org.ifsul.games4todos.service;

import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.repository.ModRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModService {
    private final ModRepository modRepository;

    public ModService(ModRepository modRepository) {
        this.modRepository = modRepository;
    }

    public Mod saveUnverified(Mod mod){
        return this.modRepository.save(mod);
    }



}
