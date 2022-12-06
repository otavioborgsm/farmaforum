package io.github.otavioborgsm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FarmaForumApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(FarmaForumApplication.class, args);
    }
}