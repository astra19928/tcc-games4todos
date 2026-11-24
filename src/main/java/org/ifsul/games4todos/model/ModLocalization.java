package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mod_localization")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModLocalization {
    @EmbeddedId
    private ModLocalizationId id;

    @ManyToOne
    @MapsId("idMod")
    @JoinColumn(name = "id_mod")
    private Mod mod;

    @ManyToOne
    @MapsId("idType")
    @JoinColumn(name = "id_type")
    private LocalizationType type;

    public ModLocalization(Mod mod, LocalizationType type) {
        this.id = new ModLocalizationId(mod.getId(), type.getId());
        this.mod = mod;
        this.type = type;
    }
}
