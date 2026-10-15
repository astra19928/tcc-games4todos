package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.Mod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModRepository extends JpaRepository<Mod, Integer> {
}
