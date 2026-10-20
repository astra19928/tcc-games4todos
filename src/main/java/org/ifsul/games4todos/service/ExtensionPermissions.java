package org.ifsul.games4todos.service;

import lombok.Getter;

@Getter
public enum ExtensionPermissions {
    IMAGE(new String[]{
            ".png",
            ".jpeg",
            ".jpg",
            ".webp"
    });

    private final String[] extensions;

    ExtensionPermissions(String[] extensions) {
        this.extensions = extensions;
    }
}
