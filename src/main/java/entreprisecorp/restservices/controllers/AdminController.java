package entreprisecorp.restservices.controllers;

import com.google.gson.Gson;
import entreprisecorp.restservices.Response;
import entreprisecorp.restservices.ResponseSuccess;
import entreprisecorp.restservices.models.admin.Admin;
import entreprisecorp.restservices.models.admin.AdminRepository;
import entreprisecorp.utils.HashUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class AdminController {

    private final AdminRepository repository;

    public AdminController(AdminRepository repository) {
        this.repository = repository;
    }

    private final AtomicLong counter = new AtomicLong();

    @PostMapping(
            path = "/admin/login",
            consumes = "application/json",
            produces = "application/json"
    )
    public Response login(@RequestBody Admin admin)
    {
        Admin connectedAdmin = repository.findByEmail(admin.getEmail());
        if(connectedAdmin != null && HashUtils.verifyUserPassword(admin.getPassword(), connectedAdmin.getPassword(), connectedAdmin.getSalt())){
            Gson gson = new Gson();
            String adminJson = gson.toJson(connectedAdmin);
            return new Response(counter.incrementAndGet(), adminJson);
        } else {
            return new Response(counter.incrementAndGet(), "wrong password or email");
        }
    }

    @PostMapping(
            path = "/admin/register",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseSuccess register(@RequestBody Admin admin)
    {
        Admin createdAdmin = new Admin(admin.getCompany(), admin.getPassword(), admin.getEmail());

        createdAdmin.setSalt(HashUtils.getSalt(30));
        createdAdmin.setPassword(HashUtils.generateSecurePassword(admin.getPassword(), createdAdmin.getSalt()));

        try{
            repository.saveAndFlush(createdAdmin);
            System.err.println("Admin registration done!");
            createdAdmin.setPassword(null);
            Gson gson = new Gson();
            String adminJson = gson.toJson(createdAdmin);
            return new ResponseSuccess(counter.incrementAndGet(), adminJson, true);
        }
        catch (Exception exception){
            System.err.println("Admin registration failed!");
            return new ResponseSuccess(counter.incrementAndGet(), "", false);
        }
    }

}
