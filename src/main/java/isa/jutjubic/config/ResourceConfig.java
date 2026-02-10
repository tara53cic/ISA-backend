package isa.jutjubic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This maps http://localhost:8082/storage/videos/filename.mp4 
        // to the actual folder on your disk
        registry.addResourceHandler("/storage/**")
                .addResourceLocations("file:storage/");
    }
}
