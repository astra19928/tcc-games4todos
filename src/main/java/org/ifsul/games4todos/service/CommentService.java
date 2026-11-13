package org.ifsul.games4todos.service;

import org.ifsul.games4todos.model.Comment;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(String content, User user, Mod mod){
        Comment comment = new Comment(
                null,
                user,
                mod,
                content,
                0,
                LocalDate.now()
        );

        return commentRepository.save(comment);
    }

    public List<Comment> findByModId(Integer modId){
        return this.commentRepository.findAllByIdMod(modId);
    }
}
