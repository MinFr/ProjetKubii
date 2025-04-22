package com.pi4j.spring.boot.sample.app.service;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatService {
    @Bean
public TomcatServletWebServerFactory servletContainer() {
   TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
    tomcat.addContextCustomizers(context -> {
       context.setPath("/");
    });
    return tomcat;
}
}
