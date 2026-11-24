package org.ifsul.games4todos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record ModDisabilityId(
        @Column(name = "id_mod") Integer idMod,
        @Column(name = "id_type") Integer idType
) {
}
