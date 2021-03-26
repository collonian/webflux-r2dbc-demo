package com.github.collonian.webflux.demo.article.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewArgumentComment {
    private Long articleId;
    private String name;
    private String body;
}
