package com.ayush.RestApiWithDatabase.Controller;

import com.ayush.RestApiWithDatabase.Entity.User;
import com.ayush.RestApiWithDatabase.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService service;

    @Autowired
    public PublicController(UserService service) {
        this.service = service;
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> addUsers(@RequestBody User user){
        try {
            service.saveNewUser(user);
            return new ResponseEntity<>(user , HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
