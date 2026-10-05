package org.ifsul.games4todos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// JPA
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user")

// LOMBOK
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
