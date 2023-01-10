package com.localhost.hellodynamo.configuration;

// Singleton Pattern

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

    @Autowired
    AppConfigBean appConfigBean;

    DynamoDB dynamoDB = null;

    public DynamoDB getDynamoDB() {
        if (dynamoDB == null) {
            synchronized (this) {
                if (dynamoDB == null) {
                    AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
//                                    appConfigBean.getEndPoint(),
//                                    appConfigBean.getRegion()
                                    "http://localhost:8000",
                                    "us-east-1"
                                    )
                            )
                            .build();
                    dynamoDB = new DynamoDB(amazonDynamoDB);
                }
            }
        }
        return dynamoDB;
    }
}
