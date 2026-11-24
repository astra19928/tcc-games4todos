package org.ifsul.games4todos.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record ModDTO (
    String gameName,
    String modName,
    String uploaderName,
    String developers,
    String description,
    LocalDate releaseDate,
    MultipartFile image,
    MultipartFile permImage,
    MultipartFile file,
    List<Integer> localizationTypes,
    List<Integer> disabilityTypes
){}
