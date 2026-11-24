package org.ifsul.games4todos.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    public ModLocalization(Integer idMod, Integer idType){
        this.id = new ModLocalizationId(idMod, idType);
    }
}
