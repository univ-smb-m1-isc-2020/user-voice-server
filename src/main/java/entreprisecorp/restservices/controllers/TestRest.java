package entreprisecorp.restservices.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRest {


    @GetMapping(
            path = "/test"
    )
    public String testRest(){
        return "TestSucces";
    }





}
