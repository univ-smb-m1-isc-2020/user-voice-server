package entreprisecorp.restservices.controllers;

import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

import entreprisecorp.restservices.models.admin.AdminRepository;
import entreprisecorp.restservices.models.user.UserRepository;
import entreprisecorp.utils.HashUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import entreprisecorp.App;
import entreprisecorp.restservices.Response;
import entreprisecorp.restservices.models.user.User;

@RestController
public class LoginController {
    private final AtomicLong counter = new AtomicLong();

    private final UserRepository repository;

    public LoginController(UserRepository repository) {
        this.repository = repository;
    }


    @PostMapping(
        path = "/user/login",
        consumes = "application/json", 
        produces = "application/json"
    )
    public Response login(@RequestBody User user)
    {
        User connectedUser = repository.findByEmail(user.getEmail());
        if(connectedUser != null && HashUtils.verifyUserPassword(user.getPassword(), connectedUser.getPassword(), connectedUser.getSalt())){
            Gson gson = new Gson();
            String userJson = gson.toJson(connectedUser);
            return new Response(counter.incrementAndGet(), userJson);
        } else {
            return new Response(counter.incrementAndGet(), "");
        }

    }
}
