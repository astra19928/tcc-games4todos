package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.Mod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModRepository extends JpaRepository<Mod, Integer> {

    @Query(value = "SELECT * FROM modification m WHERE " +
            "m.game_name LIKE %:query% OR " +
            "m.mod_name LIKE %:query% OR " +
            "CONCAT(m.game_name, ' ', m.mod_name) LIKE %:query% OR " +
            "CONCAT(m.game_name, ' (', m.mod_name, ')') LIKE %:query%", nativeQuery = true)
    List<Mod> findAllByQuery(@Param("query") String query);

}
