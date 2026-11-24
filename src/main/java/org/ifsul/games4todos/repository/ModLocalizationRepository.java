package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.LocalizationType;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.ModLocalization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModLocalizationRepository extends JpaRepository<ModLocalization, Integer> {
    List<ModLocalization> findAllById_IdMod(Integer modId);

    void deleteAllByModId(Integer id);
}
