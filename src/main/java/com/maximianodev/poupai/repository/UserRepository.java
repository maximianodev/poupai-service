package com.maximianodev.poupai.repository;

import com.maximianodev.poupai.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepository {
  private static final String DB_TABLE_NAME = "users";
  private static final String DB_ID_FIELD = "id";
  private static final String DB_EMAIL_FIELD = "email";
  private static final String DB_NAME_FIELD = "name";
  private static final String DB_PASSWORD_FIELD = "password";

  @Autowired private DynamoDbClient dynamoDbClient;

  public void save(User user) {
    Map<String, AttributeValue> item = new HashMap<>();
    item.put(DB_ID_FIELD, AttributeValue.builder().s(UUID.randomUUID().toString()).build());
    item.put(DB_NAME_FIELD, AttributeValue.builder().s(user.getName()).build());
    item.put(DB_EMAIL_FIELD, AttributeValue.builder().s(user.getEmail()).build());
    item.put(DB_PASSWORD_FIELD, AttributeValue.builder().s(user.getPassword()).build());

    PutItemRequest request = PutItemRequest.builder().tableName(DB_TABLE_NAME).item(item).build();

    dynamoDbClient.putItem(request);
  }

  public User findByEmail(String email) {
    HashMap<String, AttributeValue> key = new HashMap<>();
    key.put(":" + DB_EMAIL_FIELD, AttributeValue.builder().s(email).build());

    ScanRequest scanRequest =
        ScanRequest.builder()
            .tableName(DB_TABLE_NAME)
            .filterExpression("email = :email")
            .expressionAttributeValues(key)
            .limit(1)
            .build();

    ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

    if (scanResponse.items().isEmpty()) {
      return null;
    }

    Map<String, AttributeValue> item = scanResponse.items().get(0);

    User user = new User();
    user.setId(item.get("id").s());
    user.setName(item.get("name").s());
    user.setEmail(item.get("email").s());
    user.setPassword(item.get("password").s());

    return user;
  }
}
