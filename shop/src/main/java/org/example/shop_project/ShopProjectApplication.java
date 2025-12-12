package org.example.shop_project;

import org.example.shop_project.config.properties.RabbitMqProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RabbitMqProperties.class)
public class ShopProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShopProjectApplication.class, args);
  }
}
