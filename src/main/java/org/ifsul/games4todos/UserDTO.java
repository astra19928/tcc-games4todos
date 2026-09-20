package org.ifsul.games4todos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private boolean thingo;
    private boolean thingoB;
    private boolean thingoC;
}
