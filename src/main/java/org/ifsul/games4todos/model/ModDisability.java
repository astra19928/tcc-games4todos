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

    @MapsId("idMod")
    @ManyToOne
    @JoinColumn(name = "id_mod")
    private Mod mod;

    @MapsId("idType")
    @ManyToOne
    @JoinColumn(name = "id_type")
    private DisabilityType type;

    public ModDisability(Mod mod, DisabilityType type){
        this.id = new ModDisabilityId(mod.getId(), type.getId());
        this.mod = mod;
        this.type = type;
    }
}
