package com.ayush.RestApiWithDatabase.Service;

import com.ayush.RestApiWithDatabase.Entity.User;
import com.ayush.RestApiWithDatabase.Repo.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // if we want to use logger like this, we have to implement it

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void saveEntry(User user){
        repository.save(user);
    }

    public void saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            repository.save(user);
        }
        catch (Exception e) {
            logger.error("Logging practice{}", String.valueOf(e)); // {} - means PlaceHolder
//            logger.info("Logging practice");
//            logger.warn("Logging practice");
//            logger.debug("Logging practice");
//            logger.trace("Logging practice");
        }
    }

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER" , "ADMIN"));
        repository.save(user);
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public Optional<User> findById(ObjectId id){
        return repository.findById(id);
    }

    public void deleteById(ObjectId id){
        repository.deleteById(id);
    }

    public User findByUserName(String username) {
        return repository.findByUsername(username);
    }

}
