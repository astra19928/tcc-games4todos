package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "disability_type")
@Getter
@Setter
public class DisabilityType {
    @Id
    @Column(name = "id_type")
    private Integer id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private DisabilityCategory category;

    public String getFullName() {
        return category.getName() + "(" + name + ")";
    }
}