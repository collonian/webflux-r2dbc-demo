package com.github.collonian.webflux.demo.product;

import com.github.collonian.webflux.demo.product.vo.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
public class ProductResource {
    private final ProductRepository productRepository;

    public ProductResource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux<Product> findProducts() {
        return productRepository.findAvailable(LocalDateTime.now());
    }
}
