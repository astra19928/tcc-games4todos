package org.ifsul.games4todos;

import org.ifsul.games4todos.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
scanBasePackages = {
		"org.ifsul.games4todos.repository",
		"org.ifsul.games4todos.controller",
		"org.ifsul.games4todos.service",
		"org.ifsul.games4todos.model",
        "org.ifsul.games4todos.security",
        "org.ifsul.games4todos.storage",
        "org.ifsul.games4todos.pagecontroller"
})
@EnableConfigurationProperties(StorageProperties.class)
@EnableAsync
public class Games4TodosApplication {

	public static void main(String[] args) {
		SpringApplication.run(Games4TodosApplication.class, args);
	}

}
