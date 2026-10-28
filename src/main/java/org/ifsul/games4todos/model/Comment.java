package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// JPA
@Entity
@Table(name = "comment")

// Lombok
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_comment")
    private Integer id;

    @Column(nullable = false, name="id_uploader")
    private Integer idUploader;

    @Column(nullable = false, name="id_mod")
    private Integer idMod;

    @Column(nullable = false, name="content", length=300)
    private String content;

    @Column(nullable = false, name="likes")
    private Integer likes;

}
