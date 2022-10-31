package com.flyan.swiftmcweb.core.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 消息通信 web 应用程序
 *
 * @author flyan
 * @version 1.0
 * @date 10/27/22
 */
@SpringBootApplication
@Slf4j
public abstract class MwebApplication {

    public static void launch(Class<? extends MwebApplication> appClass, String[] args) {
        var startMs = System.currentTimeMillis();
        SpringApplication.run(appClass, args);
        var endMs = System.currentTimeMillis();
        log.info("Swiftmcweb application launch cost: {}ms", (endMs - startMs));
    }

}
