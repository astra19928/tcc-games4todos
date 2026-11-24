package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.LocalizationType;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.ModLocalization;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModLocalizationRepository extends JpaRepository<ModLocalization, Integer> {
    List<ModLocalization> findAllById_IdMod(Integer modId);

    void deleteAllByModId(Integer id);

    @Query("select ml.type.id from ModLocalization ml where ml.mod.id = :modId")
    List<Integer> findTypeIdsByModId(@Param("modId") Integer modId);

}
