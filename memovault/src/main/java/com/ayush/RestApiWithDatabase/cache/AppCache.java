package com.ayush.RestApiWithDatabase.cache;

import com.ayush.RestApiWithDatabase.Entity.ConfigJournalAppEntity;
import com.ayush.RestApiWithDatabase.Repo.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum Keys {
        WEATHER_API
    }

    private final ConfigJournalAppRepository configJournalAppRepository;

    private Map<String , String> appCache;

    @Autowired
    public AppCache(ConfigJournalAppRepository configJournalAppRepository) {
        this.configJournalAppRepository = configJournalAppRepository;
    }

    @PostConstruct
    public void init(){
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();

        for (ConfigJournalAppEntity configJournalAppEntity : all){
            appCache.put(configJournalAppEntity.getKey() , configJournalAppEntity.getValue());
        }
    }

    public String getValue(Keys key){
        return appCache.get(key.name());
    }
}
