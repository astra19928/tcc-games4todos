package org.ifsul.games4todos.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UserPatchDTO(
        String nickname,
        String password,
        MultipartFile image,
        List<Integer> disabilityTypeIds
) {}
