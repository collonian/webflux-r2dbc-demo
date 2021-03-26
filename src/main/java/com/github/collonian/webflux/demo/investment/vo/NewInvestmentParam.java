package com.github.collonian.webflux.demo.investment.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewInvestmentParam {
    private Long productId;
    private Long amount;
}
