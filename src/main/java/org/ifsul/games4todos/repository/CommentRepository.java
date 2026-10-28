package org.ifsul.games4todos.repository;

import org.ifsul.games4todos.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query(value = "SELECT * FROM comment WHERE comment.id_mod = :modId", nativeQuery = true)
    List<Comment> findAllByIdMod(@Param("modId") Integer modId);
}
