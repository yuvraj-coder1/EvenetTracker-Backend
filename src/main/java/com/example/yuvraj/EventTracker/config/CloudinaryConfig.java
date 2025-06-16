package com.example.yuvraj.EventTracker.config;


import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary getCloudinary() {
        System.out.println("Looking for .env in: " + System.getProperty("user.dir"));
//        dotenv = Dotenv.load();
        System.out.println("CLOUDINARY_URL = " + System.getenv("CLOUDINARY_URL"));
        Cloudinary cloudinary = new Cloudinary(System.getenv("CLOUDINARY_URL"));
        System.out.println(cloudinary.config.cloudName);
        return cloudinary;
    }

}
