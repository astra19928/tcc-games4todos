package org.ifsul.games4todos.security;

import lombok.Getter;

@Getter
public enum AuthorityPermissions {

    GUEST(new String[]{
            "/",
            "/login",
            "/users",
            "/index.html",
            "/login.html",
            "/header.html",
            "/modificacao.html",
            "/cadastro.html"
    }),
    USER(new String[]{
            "/config",
            "/login/logout",
            "/login/me",
            "/upload",
            "/upload.html",
            "/download"
    });

    private final String[] urls;

    private AuthorityPermissions(String[] urls){
        this.urls = urls;
    }

}
