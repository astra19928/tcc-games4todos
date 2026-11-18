package org.ifsul.games4todos.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UserDTO(
    String nickname,
    String email,
    String password,
    MultipartFile image,
    List<Integer> disabilityTypeIds
) {}
