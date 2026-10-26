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
            "/cadastro.html",
            "/api/mods/{id}",
            "/api/mods/{id}/image",
            "/mods",
            "/mods/{id}"
    }),
    USER(new String[]{
            "/api/config",
            "/api/login/logout",
            "/api/login/me",
            "/api/upload",
            "/user/upload.html",
            "/api/mods/upload",
            "/api/mods/{id}/download",

    });

    private final String[] urls;

    private AuthorityPermissions(String[] urls){
        this.urls = urls;
    }

}
