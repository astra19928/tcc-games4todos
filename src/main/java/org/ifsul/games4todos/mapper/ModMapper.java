package org.ifsul.games4todos.mapper;

public class ModMapper {
    public static String shortenDescription(String desc){
        if(desc.length() > 250)
            return desc.substring(0, 250) + "...";

        return desc;
    }
}
