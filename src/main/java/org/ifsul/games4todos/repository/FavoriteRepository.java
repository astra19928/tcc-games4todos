package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findById_IdUserAndId_IdMod(Integer idUser, Integer idMod);

    List<Favorite> findAllById_IdUser(Integer idUser);
}
