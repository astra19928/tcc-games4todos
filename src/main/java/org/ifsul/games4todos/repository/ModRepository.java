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

    List<Mod> findTop8ByOrderByReleaseDateDescIdDesc();

    @Query(value = "SELECT id_type FROM user_disability WHERE id_user = :userId",
            nativeQuery = true)
    List<Integer> findDisabilityTypeIdsByUserId(@Param("userId") Integer userId);

    @Query(value = """
        SELECT m.* FROM modification m
        JOIN mod_disability md ON md.id_mod = m.id_modification
        WHERE md.id_type IN (:typeIds)
        GROUP BY m.id_modification
        ORDER BY COUNT(*) DESC, m.release_date DESC, m.id_modification DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Mod> findBestMatches(@Param("typeIds") List<Integer> typeIds,
                              @Param("limit") int limit);

}
