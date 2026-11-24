package org.ifsul.games4todos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record FavoriteId(
        @Column(name = "id_user") Integer idUser,
        @Column(name = "id_mod") Integer idMod
) {
}
