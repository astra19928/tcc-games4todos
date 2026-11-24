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
public class Favorite {
    @EmbeddedId
    private FavoriteId id;

    @MapsId("idUser")
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @MapsId("idMod")
    @ManyToOne
    @JoinColumn(name = "id_mod")
    private Mod mod;

    public Favorite(User user, Mod mod){
        this.id = new FavoriteId(user.getId(), mod.getId());
        this.user = user;
        this.mod = mod;
    }
}
