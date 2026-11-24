package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.ModDisability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModDisabilityRepository extends JpaRepository<ModDisability, Integer> {
    List<ModDisability> findAllById_IdMod(Integer modId);

    void deleteAllByModId(Integer id);

    @Query("select md.type.id from ModDisability md where md.mod.id = :modId")
    List<Integer> findTypeIdsByModId(@Param("modId") Integer modId);
}
