package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModDisability {
    @EmbeddedId
    private ModDisabilityId id;

    @MapsId("idType")
    @ManyToOne
    @JoinColumn(name = "id_type")
    private LocalizationType type;

    public ModDisability(Integer idMod, Integer idType){
        this.id = new ModDisabilityId(idMod, idType);
    }
}
