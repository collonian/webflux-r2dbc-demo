package com.github.collonian.webflux.demo.article;

import com.github.collonian.webflux.demo.article.vo.ArticleComment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ArticleCommentRepository extends ReactiveCrudRepository<ArticleComment, Long> {
    Flux<ArticleComment> findByArticleId(Long id);
}
