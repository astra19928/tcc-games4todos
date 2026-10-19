package org.ifsul.games4todos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

// LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModDTO {
    private String gameName;
    private String modName;
    private String developers;
    private LocalDate releaseDate;
    private MultipartFile image;
    private MultipartFile permImage;
    private MultipartFile file;
}
