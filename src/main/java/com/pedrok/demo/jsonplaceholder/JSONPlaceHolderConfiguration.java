package com.pedrok.demo.jsonplaceholder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONPlaceHolderConfiguration {
    @Bean("jsonplaceholder")
    CommandLineRunner runner(JSONPlaceHolderClient jsonPlaceHolderClient) {
        return args -> {
            System.out.println("total posts: " + jsonPlaceHolderClient.getPosts().size());
            System.out.println("Post 4: " + jsonPlaceHolderClient.getPost(4L));
        };
    }
}
