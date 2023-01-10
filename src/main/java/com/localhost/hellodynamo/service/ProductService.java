package com.localhost.hellodynamo.service;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.localhost.hellodynamo.bean.Product;
import com.localhost.hellodynamo.repository.DynamoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    DynamoDbRepository dynamoDbRepository;

    public void createTable(String tableName) throws Exception {
        try {
//            if (dynamoDbRepository.getTable(tableName) == null) {
                dynamoDbRepository.createProductTable();
//            }
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void saveProduct(Product product) throws Exception {
        createTable("product");
        Table table = dynamoDbRepository.getTable("product");
        try {
            String id = UUID.randomUUID().toString();
            System.out.println(">>> New Product ID: " + id);
            PutItemOutcome outcome = table.putItem(new Item().withPrimaryKey("id", id)
                    .with("name", product.getName())
                    .with("price", product.getPrice())
                    .with("quantity", product.getQuantity())
            );
            System.out.println(">>> Product saved: " + outcome.getPutItemResult());
        }
        catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public Product getProduct(String uuid, String name) {
        Product product = null;
        Table table = dynamoDbRepository.getTable("product");
        if (table==null) {
            return null;
        }
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("id", uuid, "name", name);
        try {
            System.out.println(">>> Finding product");
            Item item = table.getItem(spec);
            if (item != null) {
                product = new Product();
                product.setId(item.getString("id"));
                product.setName(item.getString("name"));
                product.setPrice(item.getLong("price"));
                product.setQuantity(item.getInt("quantity"));
            }
            System.out.println(">>> Product search succeeded");
        }
        catch (Exception e) {
            System.err.println(">>> Unable to read product: " + uuid);
            System.err.println(e.toString());
        }
        return product;
    }

    public List<Product> getAllProducts() throws Exception {
        ScanSpec spec = new ScanSpec();
        List<Product> productList = new ArrayList<Product>();
        try {
            Table table = dynamoDbRepository.getTable("product");
            ItemCollection<ScanOutcome> items = table.scan(spec);
            Iterator<Item> itemIterator = items.iterator();
            while (itemIterator.hasNext()) {
                Item item = itemIterator.next();
                if(item == null) {
                    System.out.println(">>> Item: null");
                    continue;
                }
                System.out.println(">>> Item: " + item.toString());
                Product product = new Product();
                product.setId(item.getString("id"));
                product.setName(item.getString("name"));
                product.setPrice(item.getLong("price"));
                product.setQuantity(item.getInt("quantity"));
                productList.add(product);
            }
        }
        catch (Exception e) {
            System.err.println(">>> Unable to scan table");
            System.err.println(e.toString());
        }
        return productList;
    }

    public void updateProduct(String uuid, String name, Integer quantity) {
        UpdateItemSpec spec = new UpdateItemSpec()
                .withPrimaryKey("id", uuid, "name", name)
                .withUpdateExpression("set quantity = :quantity")
                .withValueMap(new ValueMap().withNumber(":quantity", 5))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        try {
            Table table = dynamoDbRepository.getTable("product");
            System.out.println(">>> Updating product item");
            UpdateItemOutcome outcome = table.updateItem(spec);
            System.out.println(">>> update succeeded\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println(String.format(">>> Unable to update item: %1$s, name %2$s", uuid, name));
            System.err.println(e.toString());
        }
    }

    public void deleteProduct(String uuid, String name) {
        DeleteItemSpec spec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("id", uuid, "name", name));
        try {
            Table table = dynamoDbRepository.getTable("product");
            System.out.println(">>> Deleting item: " + uuid);
            table.deleteItem(spec);
            System.out.println(">>> Item deleted successfully");
        }
        catch (Exception e) {
            System.err.println(String.format(">>> Unable to delete item: %1$s, name %2$s", uuid, name));
            System.err.println(e.toString());
        }
    }
}
