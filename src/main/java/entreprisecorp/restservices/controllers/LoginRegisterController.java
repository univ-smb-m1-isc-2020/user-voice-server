package entreprisecorp.restservices.controllers;

import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.user.User;
import entreprisecorp.restservices.models.user.UserPrincipal;
import entreprisecorp.restservices.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LoginRegisterController {
    private final AtomicLong counter = new AtomicLong();
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;


    @PostMapping(
            path = "/login"
    )
    public ResponseSuccess login(){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseSuccess(counter.incrementAndGet(),principal.getUser().toString(), true);
    }


    @PostMapping(
            path = "/register",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseSuccess register(@RequestBody User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try{
            userRepository.saveAndFlush(user);
            return new ResponseSuccess(counter.incrementAndGet(), "register succeed", true);
        }
        catch (Exception exception){
            System.err.println("Admin registration failed!");
            return new ResponseSuccess(counter.incrementAndGet(), "register fail", false);
        }
    }
}
