package entreprisecorp.restservices.controllers;

import com.google.gson.Gson;
import entreprisecorp.restservices.Response;
import entreprisecorp.restservices.models.user.User;
import entreprisecorp.restservices.models.user.UserFront;
import entreprisecorp.restservices.models.user.UserRepository;
import entreprisecorp.utils.HashUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

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
    public Response login(@RequestBody UserFront userfront)
    {

        User connectedUser = repository.findByEmail(userfront.getEmail());
        if(connectedUser != null && HashUtils.verifyUserPassword(userfront.getPassword(), connectedUser.getPassword(), connectedUser.getSalt())){
            Gson gson = new Gson();
            String userJson = gson.toJson(connectedUser);
            return new Response(counter.incrementAndGet(), userJson);
        } else {
            return new Response(counter.incrementAndGet(), "wrong password or email");
        }

    }
}
