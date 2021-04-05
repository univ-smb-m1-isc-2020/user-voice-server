package entreprisecorp.restservices.controllers;

import java.util.concurrent.atomic.AtomicLong;

import com.google.gson.Gson;

import entreprisecorp.restservices.models.admin.Admin;
import entreprisecorp.restservices.models.user.UserRepository;
import entreprisecorp.utils.HashUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entreprisecorp.App;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.user.User;

@RestController
public class RegisterController {
    private final AtomicLong counter = new AtomicLong();
    private final UserRepository repository;

    public RegisterController(UserRepository repository) {
        this.repository = repository;
    }


    @PostMapping(
        path = "/user/register",
        consumes = "application/json", 
        produces = "application/json"
    )
    public ResponseSuccess register(@RequestBody User user)
    {
        User createdUser = new User(user.getUsername(), user.getPassword(),user.getEmail());

        createdUser.setSalt(HashUtils.getSalt(30));
        createdUser.setPassword(HashUtils.generateSecurePassword(user.getPassword(), createdUser.getSalt()));

        try{
            repository.saveAndFlush(createdUser);
            System.err.println("User registration done!");
            Gson gson = new Gson();
            String userJson = gson.toJson(createdUser);
            return new ResponseSuccess(counter.incrementAndGet(), userJson, true);
        }
        catch (Exception exception){
            System.err.println("User registration failed!");
            return new ResponseSuccess(counter.incrementAndGet(), "", false);
        }

    }
}
