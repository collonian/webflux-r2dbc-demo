package com.github.collonian.webflux.demo.product;

import com.github.collonian.webflux.demo.product.vo.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    @Query("SELECT " +
            "     p.id, p.name, p.total_amount, p.started_at, p.finished_at,  " +
            "     (SELECT COALESCE(COUNT(1), 0) FROM INVESTMENT i WHERE i.product_id = p.id) investment_count ," +
            "     (SELECT COALESCE(SUM(amount), 0) FROM INVESTMENT i WHERE i.product_id = p.id) investment_amount " +
            "FROM PRODUCT p " +
            "WHERE p.started_at <= :now AND :now <= p.finished_at")
    Flux<Product> findAvailable(LocalDateTime now);

    @Query("SELECT " +
            "     p.id, p.name, p.total_amount, p.started_at, p.finished_at,  " +
            "     (SELECT COALESCE(COUNT(1), 0) FROM INVESTMENT i WHERE i.product_id = p.id) investment_count ," +
            "     (SELECT COALESCE(SUM(amount), 0) FROM INVESTMENT i WHERE i.product_id = p.id) investment_amount " +
            "FROM PRODUCT p " +
            "WHERE p.id = :productId")
    Mono<Product> findById(Long productId);
}
