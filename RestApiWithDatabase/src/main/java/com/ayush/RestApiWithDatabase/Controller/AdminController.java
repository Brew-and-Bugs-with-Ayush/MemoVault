package com.ayush.RestApiWithDatabase.Controller;

import com.ayush.RestApiWithDatabase.Entity.User;
import com.ayush.RestApiWithDatabase.Service.UserService;
import com.ayush.RestApiWithDatabase.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AppCache appCache;

    @Autowired
    public AdminController(UserService userService, AppCache appCache) {
        this.userService = userService;
        this.appCache = appCache;
    }

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getAll();

        if (all != null && !all.isEmpty()){
            return new ResponseEntity<>(all , HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUserByRole(@RequestBody User user){
        try {
            userService.saveAdmin(user);
            return new ResponseEntity<>("Admin user created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create admin" + e.getMessage() , HttpStatus.BAD_REQUEST );
        }
    }

    @GetMapping("clear-app-cache")
    public void clearAppCache(){
        appCache.init();
    }
}
