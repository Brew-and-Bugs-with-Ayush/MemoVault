package com.ayush.RestApiWithDatabase.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class HealthCheckController {

    @GetMapping("/health-check")
    public String message(){
        return "ok! ..";
    }
}
