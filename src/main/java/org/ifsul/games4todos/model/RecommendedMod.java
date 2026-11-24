package org.ifsul.games4todos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecommendedMod {
    private final Integer id;
    private final String name;
    private final String href;
    private final String iconSrc;
    private final List<String> matchedTypes;
}