package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.model.LocalizationType;
import org.ifsul.games4todos.repository.LocalizationTypeRepository;
import org.ifsul.games4todos.repository.ModLocalizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalizationService {
    private final LocalizationTypeRepository localizationTypeRepository;
    private final ModLocalizationRepository modLocalizationRepository;

    public List<LocalizationType> getAllTypes() {
        return localizationTypeRepository.findAll();
    }

    public List<LocalizationType> getAllTypesByModId(Integer modId) {
        return modLocalizationRepository.findAllById_IdMod(modId);
    }

}
