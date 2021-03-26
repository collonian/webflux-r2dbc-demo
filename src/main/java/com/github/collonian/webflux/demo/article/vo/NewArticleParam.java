package com.github.collonian.webflux.demo.article.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class NewArticleParam {
    private String name;
    private String title;
    private String body;
}
