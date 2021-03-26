package com.github.collonian.webflux.demo.product.vo;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
@Builder
public class Product {
    @Id
    private Long id;

    private String name;
    private Long totalAmount;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private Long investmentCount;
    private Long investmentAmount;
}
