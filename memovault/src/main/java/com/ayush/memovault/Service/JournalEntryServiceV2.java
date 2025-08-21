package com.ayush.memovault.Service;

import com.ayush.memovault.Entity.JournalEntity;
import com.ayush.memovault.Entity.User;
import com.ayush.memovault.Repo.JournalRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class JournalEntryServiceV2 {

   private final JournalRepo repo;
   private final UserService userService;

   @Autowired
    public JournalEntryServiceV2(JournalRepo repo , UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    public List<JournalEntity> getAllEntries(){
        return repo.findAll();
    }

    public Optional<JournalEntity> getEntryById(ObjectId id) {
        return repo.findById(id);
    }

    @Transactional
    public void addEntry(JournalEntity entity, String username) {
       try {

           User user = userService.findByUserName(username);

           entity.setDate(LocalDateTime.now());

           JournalEntity saved = repo.save(entity);
           user.getEntities().add(saved);
           userService.saveEntry(user);

       } catch (Exception e) {
           log.error("Exception" , e);
           throw new IllegalArgumentException("An error occurred while saving the entity" , e);
       }

    }

    @Transactional
    public void updateEntryById(JournalEntity entity) {
       try {
           entity.setDate(LocalDateTime.now());
           repo.save(entity);
       } catch (Exception e) {
           log.error("Exception" , e);
       }
    }

    @Transactional
    public void deleteEntry(ObjectId id, String username) {
       try {


           User user = userService.findByUserName(username);
           boolean removed = user.getEntities().removeIf(x -> x.getId().equals(id));

           if (removed) {
               userService.saveEntry(user);
               repo.deleteById(id);
           }
       }
       catch (Exception e){
           log.error("e: ", e);
           throw new IllegalArgumentException("An error occurred while deleting the entries. " + e);
       }
    }
}
