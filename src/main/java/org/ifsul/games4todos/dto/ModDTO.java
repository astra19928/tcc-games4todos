package org.ifsul.games4todos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record ModDTO (
    String gameName,
    String modName,
    String uploaderName,
    String developers,
    String description,
    LocalDate releaseDate,
    MultipartFile image,
    MultipartFile permImage,
    MultipartFile file
){}
