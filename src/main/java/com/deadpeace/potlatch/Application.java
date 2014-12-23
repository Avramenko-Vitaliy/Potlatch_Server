package com.deadpeace.potlatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * Created with IntelliJ IDEA.
 * User: DeadPeace
 * Date: 13.10.2014
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultiPartConfigFactory factory = new MultiPartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("10MB");
        return factory.createMultipartConfig();
    }
}