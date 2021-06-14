package org.vaugneuray.amphizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
public class AmphizzaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmphizzaApplication.class, args);
    }

}
