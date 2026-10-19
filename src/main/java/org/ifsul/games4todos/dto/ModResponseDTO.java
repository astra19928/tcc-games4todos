package org.ifsul.games4todos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ModResponseDTO {
    private String developers;
    private String uploader;
    private LocalDate date;
    private Float fileSize;
}
