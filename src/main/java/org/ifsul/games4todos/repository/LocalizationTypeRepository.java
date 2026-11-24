package org.ifsul.games4todos.repository;


import org.ifsul.games4todos.model.LocalizationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizationTypeRepository extends JpaRepository<LocalizationType, Integer> {
}
