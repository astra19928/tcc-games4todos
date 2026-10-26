package org.ifsul.games4todos.dto;

import jakarta.persistence.Column;
import org.ifsul.games4todos.model.User;
import org.springframework.web.multipart.MultipartFile;

public record UserDTO(
    String nickname,
    String email,
    String password,
    MultipartFile image
) {}
