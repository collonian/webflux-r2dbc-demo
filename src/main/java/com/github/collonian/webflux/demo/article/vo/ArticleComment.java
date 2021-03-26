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
public final class ArticleComment {
    @Id
    private Long id;

    private Long articleId;

    private String name;
    private String body;

    public static ArticleComment from (NewArgumentComment param) {
        ArticleComment comment = new ArticleComment();
        comment.articleId = param.getArticleId();
        comment.name = param.getName();
        comment.body = param.getBody();
        return comment;
    }

}
