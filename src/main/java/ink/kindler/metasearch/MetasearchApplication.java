package ink.kindler.metasearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MetasearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetasearchApplication.class, args);
	}

}
