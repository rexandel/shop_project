package org.example.shop;

import org.example.shop.config.properties.KafkaSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(KafkaSettings.class)
public class ShopProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShopProjectApplication.class, args);
  }
}
