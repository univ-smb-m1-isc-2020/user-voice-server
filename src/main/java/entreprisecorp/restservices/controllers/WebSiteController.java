package entreprisecorp.restservices.controllers;

import com.google.gson.Gson;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.apikeys.WebSite;
import entreprisecorp.restservices.models.apikeys.WebSiteRepository;
import entreprisecorp.restservices.models.user.User;
import entreprisecorp.restservices.models.user.UserPrincipal;
import entreprisecorp.restservices.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class WebSiteController {
    private final AtomicLong counter = new AtomicLong();
    Gson gson = new Gson();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebSiteRepository webSiteRepository;

    @PostMapping(
            path = "/site/create",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseSuccess createSite(@RequestBody WebSite webSite){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        webSite.setOwner(user);
        webSite.setApiKey(webSite.generateAPIKEY());

        try{
            webSiteRepository.saveAndFlush(webSite);
            String response = gson.toJson(webSite);
            return new ResponseSuccess(counter.incrementAndGet(),response, true);
        }
        catch (Exception exception){
            return new ResponseSuccess(counter.incrementAndGet(), "register website failed", false);
        }


    }

    @PostMapping(
            path = "/site/get/user"
    )
    public ResponseSuccess getsitesbyUser(){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();


        try{
            ArrayList<WebSite> websites = webSiteRepository.findAllByOwner(user);
            String response = gson.toJson(websites);
            return new ResponseSuccess(counter.incrementAndGet(),response, true);
        }
        catch (Exception exception){
            return new ResponseSuccess(counter.incrementAndGet(), "no websites", false);
        }


    }

}
