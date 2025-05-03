package com.maximianodev.poupai.config;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {
  @Bean
  @Profile("prod")
  public DynamoDbClient dynamoDbClientProduction() {
    return DynamoDbClient.builder().region(Region.US_EAST_2).build();
  }

  @Bean
  @Profile("local")
  public DynamoDbClient dynamoDbClientLocal() {
    return DynamoDbClient.builder()
        .endpointOverride(
            URI.create("http://localhost:8000")
            )
        .region(Region.US_EAST_2)
        .build();
  }
}
