package org.ifsul.games4todos.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

// SPRING
@ConfigurationProperties("storage")

//LOMBOK
@Getter
@Setter
public class StorageProperties {

    private String location = "uploads";


}