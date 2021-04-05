package entreprisecorp;

import entreprisecorp.database.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static ELOHandler eloHandler;
	public static void main(String[] args) {
		eloHandler = new ELOHandler();
		SpringApplication.run(App.class, args);
	}
}