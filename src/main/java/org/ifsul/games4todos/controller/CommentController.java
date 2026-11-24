package org.ifsul.games4todos.controller;

import lombok.AllArgsConstructor;
import org.ifsul.games4todos.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, Authentication auth){
        commentService.delete(id, auth);
    }
}
