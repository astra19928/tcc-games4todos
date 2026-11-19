package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class DisabilityCategory {
    @Id
    @Column(name = "id_category")
    private Integer id;

    @Column(name = "name")
    private String name;
}
