package org.example.shop;

import org.example.shop.config.properties.RabbitMqProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(RabbitMqProperties.class)
public class ShopProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShopProjectApplication.class, args);
  }
}
