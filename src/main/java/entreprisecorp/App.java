package entreprisecorp;

import entreprisecorp.database.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import entreprisecorp.database.AdminDbHandler;
import entreprisecorp.database.Database;
import entreprisecorp.database.ELOHandler;
import entreprisecorp.database.FeaturesDbHandler;
import entreprisecorp.database.UserDbHandler;

@SpringBootApplication
public class App {

	public static ELOHandler eloHandler;
	public static void main(String[] args) {
		eloHandler = new ELOHandler();
		SpringApplication.run(App.class, args);
	}
}