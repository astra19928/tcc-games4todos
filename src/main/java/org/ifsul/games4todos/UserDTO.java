package org.ifsul.games4todos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private String nickname;
    private String email;
    private String password;
}
