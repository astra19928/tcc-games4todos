package org.ifsul.games4todos.service;

import org.ifsul.games4todos.model.Comment;
import org.ifsul.games4todos.model.Mod;
import org.ifsul.games4todos.model.User;
import org.ifsul.games4todos.repository.CommentRepository;
import org.ifsul.games4todos.security.G4TUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public void delete(Integer id, Authentication auth) {
        G4TUserDetails details = (G4TUserDetails) auth.getPrincipal();
        if(details == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Você deve estar autenticado para deletar uma modificação!");

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comentário não encontrado"));
        User user = details.getUser();
        User commentUser = comment.getUser();

        if(!commentUser.getId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode deletar comentários de outros usuário!");

        commentRepository.delete(comment);
    }
}
