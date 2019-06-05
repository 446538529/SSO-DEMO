package sso.demo.ssoclient1.resource.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sso.demo.ssoclient1.resource.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ResourceController {
    @GetMapping("/list/user")
    public List<User> getUserList(){
        return new ArrayList<>(Arrays.asList(new User("AA",11),new User("BB",12)));
    }
}
