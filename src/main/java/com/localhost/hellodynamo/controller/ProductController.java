package com.localhost.hellodynamo.controller;

import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.localhost.hellodynamo.bean.Product;
import com.localhost.hellodynamo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping(value = "saveProduct", consumes = "application/json")
    public ResponseEntity saveProduct(@RequestBody Product product) {
        try {
            productService.saveProduct(product);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getproduct", produces = {"application/json"})
    public ResponseEntity<Product> getProduct(@PathParam("id") String id, @PathParam("name") String name) {
        try {
            return new ResponseEntity(productService.getProduct(id, name), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getproducts", produces = {"application/json"})
    public ResponseEntity<List<Product>> getProducts() {
        try {
            return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/updateproduct")
    public ResponseEntity updateProduct(@PathParam("id") String id, @PathParam("name") String name, @PathParam("quantity") Integer quantity) {
        try {
            productService.updateProduct(id, name, quantity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value="/deleteproduct")
    public ResponseEntity deleteProduct(@PathParam("id") String id, @PathParam("name") String name) {
        try {
            productService.deleteProduct(id, name);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
