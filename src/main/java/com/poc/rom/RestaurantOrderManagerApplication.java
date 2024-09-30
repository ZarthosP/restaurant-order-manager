package com.poc.rom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableJpaRepositories("com.poc.rom")
//@EntityScan("com.poc.rom")
//@EnableTransactionManagement
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableJpaAuditing
public class RestaurantOrderManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantOrderManagerApplication.class, args);
	}

}
