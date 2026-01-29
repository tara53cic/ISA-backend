package isa.jutjubic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class JutjubicApplication {

	public static void main(String[] args) {
		SpringApplication.run(JutjubicApplication.class, args);
	}

}
