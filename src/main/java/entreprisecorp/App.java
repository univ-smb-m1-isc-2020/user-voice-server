package entreprisecorp;

import entreprisecorp.database.*;
import entreprisecorp.hibernate.UserRepository;
import entreprisecorp.hibernate.UserRepositoryTest;
import entreprisecorp.restservices.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class App {

	public static UserDbHandler userDbHandler;
	public static FeaturesDbHandler featuresDbHandler;
	public static AdminDbHandler adminDbHandler;
	public static ELOHandler eloHandler;

	public static void main(String[] args) {

		//init singleton database
		Database database = new Database();

		userDbHandler = new UserDbHandler();
		featuresDbHandler = new FeaturesDbHandler();
		adminDbHandler = new AdminDbHandler();
		eloHandler = new ELOHandler();

		SpringApplication.run(App.class, args);
	}
}