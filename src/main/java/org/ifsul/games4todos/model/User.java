package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.*;

// JPA
@Entity
@Table(name = "user")

// LOMBOK
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    @Column(nullable = false, length = 45)
    private String nickname;

    @Column(nullable = false, unique = true, length = 250)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

}
