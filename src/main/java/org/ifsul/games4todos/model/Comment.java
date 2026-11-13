package org.ifsul.games4todos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// JPA
@Entity
@Table(name = "comment")

// Lombok
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_comment")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_uploader")
    private User user;

    @ManyToOne
    @JoinColumn(name="id_mod")
    private Mod mod;

    @Column(nullable = false, name="content", length=300)
    private String content;

    @Column(nullable = false, name="likes")
    private Integer likes;

    @Column(name="send_date")
    private LocalDate sendDate;

    public String getUserIconSrc() {
        if(user == null)
            return "#";

        int userID = user.getId();

        return "/api/users/" + userID + "/image";
    }

    public String getUserName(){
        if(user == null)
            return "Usuário deletado";

        return user.getNickname();
    }

    public String stringSendDate(){
        return sendDate.toString();
    }
}
