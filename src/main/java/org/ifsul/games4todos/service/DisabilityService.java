package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.model.DisabilityCategory;
import org.ifsul.games4todos.model.ModDisability;
import org.ifsul.games4todos.repository.DisabilityCategoryRepository;
import org.ifsul.games4todos.repository.ModDisabilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DisabilityService {
    private final DisabilityCategoryRepository disabilityCategoryRepository;
    private final ModDisabilityRepository modDisabilityRepository;

    public List<DisabilityCategory> findAllCategories(){
        return disabilityCategoryRepository.findAll();
    }

    public List<ModDisability> findAllByModId(Integer modId){
        return modDisabilityRepository.findAllById_IdMod(modId);
    }
}
