package com.pedrok.customerservice.jsonplaceholder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONPlaceHolderConfiguration {
    @Value("${app.showJsonPlaceHolder:false}")
    private boolean showJsonPlaceHolder;

    @Bean("jsonplaceholder")
    CommandLineRunner runner(JSONPlaceHolderClient jsonPlaceHolderClient) {
        return args -> {
            if(showJsonPlaceHolder) {
                System.out.println("total posts: " + jsonPlaceHolderClient.getPosts().size());
                System.out.println("Post 4: " + jsonPlaceHolderClient.getPost(4L));
            }
        };
    }
}
