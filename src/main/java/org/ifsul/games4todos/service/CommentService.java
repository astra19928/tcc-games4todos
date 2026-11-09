package org.ifsul.games4todos.service;

import org.ifsul.games4todos.model.Comment;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(String content, User user, Integer modId){
        Comment comment = new Comment(
                null,
                user,
                modId,
                content,
                0,
                null
        );

        return commentRepository.save(comment);
    }

    public List<Comment> findByModId(Integer modId){
        return this.commentRepository.findAllByIdMod(modId);
    }
}
