package org.ifsul.games4todos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ModResponseDTO {
    private String modName;
    private String gameName;
    private String developers;
    private String uploader;
    private Integer downloads;
    private LocalDate date;
    private Float fileSize;
}
