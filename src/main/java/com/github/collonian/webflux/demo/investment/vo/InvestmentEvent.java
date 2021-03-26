package com.github.collonian.webflux.demo.investment.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
public class InvestmentEvent {
    private String id;
    private Long productId;
    private Long amount;
    private LocalDateTime createdAt;

    public static InvestmentEvent from(NewInvestmentParam param) {
        InvestmentEvent event = new InvestmentEvent();
        event.id = UUID.randomUUID().toString();
        event.productId = param.getProductId();
        event.amount = param.getAmount();
        return event;
    }

    public static InvestmentEvent invert(InvestmentEvent param) {
        InvestmentEvent event = new InvestmentEvent();
        event.id = UUID.randomUUID().toString();
        event.productId = param.getProductId();
        event.amount = param.getAmount() * -1;
        return event;
    }
}
