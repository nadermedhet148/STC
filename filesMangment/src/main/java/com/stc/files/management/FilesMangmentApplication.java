package com.stc.files.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication()
@EnableJpaRepositories(basePackages = {"com.stc.files.management.infra.repository"})
@EnableTransactionManagement
public class FilesMangmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilesMangmentApplication.class, args);
	}

}
