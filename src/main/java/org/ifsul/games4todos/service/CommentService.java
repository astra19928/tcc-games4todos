package org.ifsul.games4todos.service;

import org.ifsul.games4todos.model.Comment;
import org.ifsul.games4todos.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(String content, Integer userId, Integer modId){
        Comment comment = new Comment(
                null,
                userId,
                modId,
                content,
                0
        );

        return commentRepository.save(comment);
    }
}
