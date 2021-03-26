package com.github.collonian.webflux.demo.article.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public final class Article {
    @Id
    private Long id;

    private String name;
    private String title;
    private String body;

    public static Article from(NewArticleParam articleParam) {
        Article article = new Article();
        article.name = articleParam.getName();
        article.title = articleParam.getTitle();
        article.body = articleParam.getBody();
        return article;
    }
}
