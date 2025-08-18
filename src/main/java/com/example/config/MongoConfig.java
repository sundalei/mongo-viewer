package com.example.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

  @Bean
  MongoClient mongoClient() {
    String connectionString =
        "mongodb+srv://analytics:oarnud9I@analytics.fegzyn1.mongodb.net/?retryWrites=true&w=majority&appName=analytics";
    return MongoClients.create(
        MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .build());
  }
}
