package org.ifsul.games4todos.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record ModUpdateDTO(
        String modName, String developers, String description, LocalDate releaseDate,
        MultipartFile file, MultipartFile image,
        List<Integer> localizationTypes, List<Integer> disabilityTypes) {}