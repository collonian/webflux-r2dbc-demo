package com.github.collonian.webflux.demo.investment;

import com.github.collonian.webflux.demo.investment.vo.Investment;
import com.github.collonian.webflux.demo.investment.vo.InvestmentEvent;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public class InvestmentRepository {

    private final DatabaseClient databaseClient;

    public InvestmentRepository(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    Mono<Integer> markInvestmentEvent(InvestmentEvent event) {
        return databaseClient.sql("INSERT INTO investment_event (" +
                "       id, product_id, amount, created_at" +
                ") VALUES (" +
                "       :id, :productId, :amount, now() " +
                ")")
                .bind("id", event.getId())
                .bind("productId", event.getProductId())
                .bind("amount", event.getAmount())
                .fetch()
                .rowsUpdated();
    }

    Mono<Boolean> isInvestmentAccepted(InvestmentEvent event)  {
        return databaseClient.sql("SELECT " +
                "    (" +
                "       SELECT COALESCE(SUM(amount), 0) " +
                "       FROM investment_event sub_event " +
                "       WHERE sub_event.product_id = event.product_id " +
                "         AND sub_event.created_at <= event.created_at " +
                "    ) <= (" +
                "       SELECT COALESCE(total_amount, 0) " +
                "       FROM product p " +
                "       WHERE p.id = event.product_id " +
                "    )" +
                "FROM investment_event event " +
                "WHERE event.id = :id ")
                .bind("id", event.getId())
                .map(r -> r.get(0, Boolean.class))
                .first();
    }

    Mono<Integer> markInvestment(Investment investment)  {
        return databaseClient.sql("INSERT INTO INVESTMENT (" +
                "       id, product_id, amount, created_at " +
                ") VALUES (" +
                "       :id, :productId, :amount, now() " +
                ")")
                .bind("id", investment.getId())
                .bind("productId", investment.getProductId())
                .bind("amount", investment.getAmount())
                .fetch()
                .rowsUpdated();
    }

    Mono<Investment> findById(String id)  {
        return databaseClient
                .sql("SELECT " +
                        "     i.id, i.product_id, i.amount, i.created_at,  " +
                        "     p.name product_name, p.total_amount " +
                        "FROM investment i INNER JOIN product p on i.product_id = p.id " +
                        "WHERE i.id = :id"
                )
                .bind("id", id)
                .map(r -> Investment.builder()
                        .id(r.get("id", String.class))
                        .productId(r.get("product_id", Long.class))
                        .amount(r.get("amount", Long.class))
                        .createdAt(r.get("created_at", LocalDateTime.class))
                        .productName(r.get("product_name", String.class))
                        .totalAmount(r.get("total_amount", Long.class))
                        .build())
                .first();
    }
}
