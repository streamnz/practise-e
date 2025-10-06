package com.streamnz.practisee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy  // 启用 AOP
public class PractiseEApplication {

    public static void main(String[] args) {
        SpringApplication.run(PractiseEApplication.class, args);
    }

}
