package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_disability")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDisability {
    @EmbeddedId
    private UserDisabilityId id;

    @MapsId("idUser")
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "id_type", nullable = false)
    private Integer idType;

    public UserDisability(User user, Integer idCategory, Integer idType){
        this.user = user;
        this.id = new UserDisabilityId(user.getId(), idCategory);
        this.idType = idType;
    }

}