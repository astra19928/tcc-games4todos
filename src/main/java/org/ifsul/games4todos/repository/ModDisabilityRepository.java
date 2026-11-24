package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.ModDisability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModDisabilityRepository extends JpaRepository<ModDisability, Integer> {
    List<ModDisability> findAllById_IdMod(Integer modId);
}
