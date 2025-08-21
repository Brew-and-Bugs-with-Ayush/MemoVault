package com.ayush.RestApiWithDatabase.Controller;

import com.ayush.RestApiWithDatabase.Entity.JournalEntity;
import com.ayush.RestApiWithDatabase.Entity.User;
import com.ayush.RestApiWithDatabase.Service.JournalEntryServiceV2;
import com.ayush.RestApiWithDatabase.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryServiceV2 service2;
    private final UserService userService;

    @Autowired
    public JournalEntryController(JournalEntryServiceV2 service2 , UserService userService) {
        this.service2 = service2;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllEntriesOfUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUserName(username);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<JournalEntity> all = user.getEntities();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @GetMapping("/getEntry/{id}")
    public ResponseEntity<JournalEntity> getEntryById(@PathVariable ObjectId id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUserName(username);

        List<JournalEntity> collect = user.getEntities()
                .stream()
                .filter(x -> x.getId().equals(id)).toList();

        if(!collect.isEmpty()){
            Optional<JournalEntity> entity=  service2.getEntryById(id);

            if (entity.isPresent()){
                return new ResponseEntity<>(entity.get() , HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add")
    public ResponseEntity<JournalEntity> addEntry(@RequestBody  JournalEntity entity){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            service2.addEntry(entity , username);
            return new ResponseEntity<>(entity , HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id , @RequestBody JournalEntity newEntity){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUserName(username);

        List<JournalEntity> collect = user.getEntities()
                .stream()
                .filter(x -> x.getId().equals(id)).toList();

        if(!collect.isEmpty()){
            Optional<JournalEntity> entity=  service2.getEntryById(id);

            if (entity.isPresent()){
               JournalEntity old =  entity.get();
                old.setTitle(!newEntity.getTitle().isEmpty() ? newEntity.getTitle() : old.getTitle());
                old.setContent(newEntity.getContent() != null && !newEntity.getContent().isEmpty() ? newEntity.getContent() : old.getContent());
                service2.updateEntryById(old);
                return new ResponseEntity<>(entity.get() , HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Failed to Update" , HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable ObjectId id ){
       Optional<JournalEntity> entity =  service2.getEntryById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

       if (entity.isPresent()) {
           service2.deleteEntry(id , username);
           return new ResponseEntity<>("Entry deleted" , HttpStatus.OK);
       }
       return new ResponseEntity<>("Entry not Found" , HttpStatus.NOT_FOUND);
    }
}
