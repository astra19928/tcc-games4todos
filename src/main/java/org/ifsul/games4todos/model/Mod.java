package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

//JPA
@Entity
@Table(name = "modification")

//LOMBOK
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modification")
    private Integer id;

    @Column(nullable = false, name="id_uploader")
    private Integer idUploader;

    @Column(nullable = false, length = 100, name="game_name")
    private String gameName;

    @Column(nullable = false, length = 50, name="name_developers")
    private String nameDevelopers;

    @Column(nullable = false, length = 100, name="mod_name")
    private String modName;

    @Column(name="description")
    private String description;

    @Column(nullable = false, name="downloads")
    private Integer downloads;

    @Column(nullable = false, name="release_date")
    private LocalDate releaseDate;

    @Column(nullable = false, name="file_size")
    private Float fileSize;

    @Column(nullable = false, name="verified")
    private Boolean verified;
}
