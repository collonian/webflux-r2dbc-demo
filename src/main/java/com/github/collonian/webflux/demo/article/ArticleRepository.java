package com.github.collonian.webflux.demo.article;

import com.github.collonian.webflux.demo.article.vo.Article;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ReactiveCrudRepository<Article, Long> {
}
