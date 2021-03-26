package com.github.collonian.webflux.demo.investment.vo;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
@Builder
public class Investment {
    @Id
    private String id;
    private Long productId;
    private Long amount;
    private LocalDateTime createdAt;

    private String productName;
    private Long totalAmount;

    public static Investment from(InvestmentEvent event) {
        Investment investment = new Investment();
        investment.id = UUID.randomUUID().toString();
        investment.productId = event.getProductId();
        investment.amount = event.getAmount();
        return investment;
    }
}
