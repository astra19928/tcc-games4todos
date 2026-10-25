package org.ifsul.games4todos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public record ModResponseDTO(
    String gameName,
    String modName,
    String uploader,
    String developers,
    String description,
    LocalDate date,
    Integer downloads,
    Float fileSize
){}
