package com.github.collonian.webflux.demo.article;

import com.github.collonian.webflux.demo.article.vo.Article;
import com.github.collonian.webflux.demo.article.vo.ArticleComment;
import com.github.collonian.webflux.demo.article.vo.NewArticleParam;
import com.github.collonian.webflux.demo.article.vo.NewArgumentComment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/articles")
public class ArticleResource {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;


    public ArticleResource(ArticleRepository articleRepository, ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @GetMapping
    public Flux<Article> findAll() {
        return articleRepository.findAll();
    }

    @PostMapping
    public Mono<ResponseEntity<Article>> creteArticle(@RequestBody NewArticleParam articleParam, UriComponentsBuilder builder) {
        return articleRepository.save(Article.from(articleParam))
                .map(article -> {
                    URI location = builder.path("/api/articles/{articleId}").build(article.getId() );
                    return ResponseEntity.created(location).body(article);
                })
                ;
    }

    @GetMapping("/{articleId}")
    public Mono<Article> findOne(@PathVariable("articleId") Long articleId) {
        return articleRepository.findById(articleId);
    }

    @GetMapping("/{articleId}/comments")
    public Flux<ArticleComment> findComments(@PathVariable("articleId") Long articleId) {
        return articleCommentRepository.findByArticleId(articleId);
    }

    @PostMapping("/{articleId}/comments")
    public Mono<ResponseEntity<ArticleComment>> createComment(@PathVariable("articleId") Long articleId, @RequestBody NewArgumentComment param, UriComponentsBuilder builder) {
        if(!articleId.equals(param.getArticleId())) {
            throw new IllegalArgumentException("ArticleID Mismatched.");
        }
        return articleCommentRepository.save(ArticleComment.from(param))
                .map(comment -> {
                    URI location = builder
                            .path("/api/articles/{articleId}/comments/{commentId}")
                            .build(articleId, comment.getId());
                    return ResponseEntity.created(location).body(comment);
                })
                ;
    }

    @GetMapping("/{articleId}/comments/{commentId}")
    public Mono<ArticleComment> findCommentById(@PathVariable("commentId") Long commentId) {
        return articleCommentRepository.findById(commentId);
    }
}
