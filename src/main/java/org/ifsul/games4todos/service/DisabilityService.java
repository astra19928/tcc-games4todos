package org.ifsul.games4todos.service;

import lombok.RequiredArgsConstructor;
import org.ifsul.games4todos.model.DisabilityCategory;
import org.ifsul.games4todos.repository.DisabilityCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DisabilityService {
    private final DisabilityCategoryRepository disabilityCategoryRepository;

    public List<DisabilityCategory> findALlCategories(){
        return disabilityCategoryRepository.findAll();
    }
}
