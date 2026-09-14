package org.ifsul.games4todos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Games4todosApplication {

	public static void main(String[] args) {
		SpringApplication.run(Games4todosApplication.class, args);
	}

}
