package com.mnnu.examine;

import com.google.gson.Gson;
import com.mnnu.examine.modules.polyv.PolyvInitiation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableAsync
public class ExamineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamineApplication.class, args);
        PolyvInitiation.initPolyvLive();//初始化保利威配置--Geralt
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
