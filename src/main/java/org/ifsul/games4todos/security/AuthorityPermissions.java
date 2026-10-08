package org.ifsul.games4todos.security;

import lombok.Getter;

@Getter
public enum AuthorityPermissions {

    GUEST(new String[]{
            "/",
            "/api/login",
            "/api/users",
            "/index.html",
            "/login.html",
            "/header.html",
            "/modificacao.html",
            "/cadastro.html"
    }),
    USER(new String[]{
            "/api/config",
            "/api/login/logout",
            "/api/login/me",
            "/api/upload",
            "/upload.html",
            "/api/download"
    });

    private final String[] urls;

    private AuthorityPermissions(String[] urls){
        this.urls = urls;
    }

}
