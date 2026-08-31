package com.cws.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cws.shop.model.Product;
import com.cws.shop.repository.ProductRepository;

@RestController
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
    private ProductRepository productRepository;

    @GetMapping("/search")
    public List<Product> search(
            @RequestParam String keyword){

        return productRepository
                .findByNameContaining(keyword);
    }

    @GetMapping("/se")
    public String search(){

        return "server working";
    }

}
