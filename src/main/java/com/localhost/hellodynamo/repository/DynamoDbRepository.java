package com.localhost.hellodynamo.repository;

import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.localhost.hellodynamo.configuration.DynamoDbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class DynamoDbRepository {
    @Autowired
    DynamoDbConfig dynamoDbConfig;

    public void createProductTable() throws Exception {
        try {
            System.out.println(">>> Creating product table: product");
            Table productTable = dynamoDbConfig.getDynamoDB().createTable("product",
                    Arrays.asList(
                            new KeySchemaElement("id", KeyType.HASH),
                            new KeySchemaElement("name", KeyType.RANGE)
                    ),
                    Arrays.asList(
                            new AttributeDefinition("id", ScalarAttributeType.S),
                            new AttributeDefinition("name", ScalarAttributeType.S)
                    ),
                    new ProvisionedThroughput(10L, 10L)
            );
            productTable.waitForActive();
            System.out.println("Table created. Status: " + productTable.getDescription().getTableStatus());
        }
        catch (Exception e) {
            System.err.println("Cannot create table: " + e.toString());
            throw new Exception("Error occurred");
        }
    }

    public Table getTable(String tableName) {
        Table table = dynamoDbConfig.getDynamoDB().getTable(tableName);
        return table;
    }

    public void deleteTable(String tableName) throws Exception {
        Table table = dynamoDbConfig.getDynamoDB().getTable(tableName);
        try {
            System.out.println(">>> Attempting to delete table: product");
            table.delete();
            table.waitForDelete();;
            System.out.println(">>> Table deleted: product");
        }
        catch (Exception e) {
            System.err.println("Error deleting table: " + e.toString());
            throw new Exception("Error occurred");
        }
    }
}
