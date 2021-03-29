package entreprisecorp.hibernate;

import entreprisecorp.restservices.models.User;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRepositoryTest {
    @Autowired
    public UserRepository userRepository;


    @GetMapping("/save")
    public void Test(){

        entreprisecorp.restservices.models.User user1= new User("Alice", "Alice@test.com");
        userRepository.save(user1);
    }
}
