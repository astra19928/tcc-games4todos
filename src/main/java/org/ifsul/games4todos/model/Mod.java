package org.ifsul.games4todos.model;

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
public class Mod {
    private String gameName;
    private String modName;
    private String developers;
    private LocalDate releaseDate;
    private MultipartFile image;
    private MultipartFile file;
}
