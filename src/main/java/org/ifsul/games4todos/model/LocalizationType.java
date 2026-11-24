package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class LocalizationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localization_type")
    private Integer id;

    private String name;

    public String getFullName(){
        return "Localização (" + name + ")";
    }
}
