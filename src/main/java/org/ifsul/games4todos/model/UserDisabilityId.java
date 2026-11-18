package org.ifsul.games4todos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record UserDisabilityId(
        @Column(name = "id_user") Integer idUser,
        @Column(name = "id_category") Integer idCategory
) implements Serializable {}