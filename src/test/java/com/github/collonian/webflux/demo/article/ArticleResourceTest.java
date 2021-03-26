package com.github.collonian.webflux.demo.article;

import com.github.collonian.webflux.demo.article.vo.Article;
import com.github.collonian.webflux.demo.article.vo.ArticleComment;
import com.github.collonian.webflux.demo.article.vo.NewArticleParam;
import com.github.collonian.webflux.demo.article.vo.NewArgumentComment;
import com.github.collonian.webflux.demo.config.DemoWebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ArticleResource.class)
@Import(DemoWebSecurityConfig.class)
class ArticleResourceTest {
    @Autowired
    private WebTestClient client;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private ArticleCommentRepository articleCommentRepository;

    @Test
    @WithMockUser
    void findAll() {
        when(articleRepository.findAll())
                .thenReturn(Flux.fromIterable(Arrays.asList(
                        new Article(1L,"name","title","body"),
                        new Article(2L,"name","title","body"),
                        new Article(3L,"name","title","body")
                )));


        WebTestClient.ResponseSpec responseSpec = client.get()
                .uri("/api/articles")
                .exchange();

        responseSpec.expectStatus().isOk()
                .expectBodyList(Article.class)
                .hasSize(3);
        verify(articleRepository).findAll();
    }

    @Test
    @WithMockUser
    void creteArticle() {
        when(articleRepository.save(any()))
                .thenReturn(Mono.just(new Article(10L, "someName", "someTitle", "someBody")));

        NewArticleParam param = new NewArticleParam("someName", "someTitle", "someBody");
        WebTestClient.ResponseSpec response = client.post().uri("/api/articles")
                .bodyValue(param)
                .exchange();

        response.expectStatus().isCreated()
                .expectBody(Article.class)
                .value(Article::getName, equalTo("someName"))
                .value(Article::getTitle, equalTo("someTitle"))
                .value(Article::getBody, equalTo("someBody"))
        ;

        ArgumentCaptor<Article> captor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepository).save(captor.capture());
        assertEquals("someName", captor.getValue().getName());
        assertEquals("someTitle", captor.getValue().getTitle());
    }

    @Test
    @WithMockUser
    void findOne() {
        when(articleRepository.findById(eq(10L)))
                .thenReturn(Mono.just(new Article(10L, "someName", "someTitle", "someBody")));

        WebTestClient.ResponseSpec result = client.get()
                .uri("/api/articles/10")
                .exchange();

        result.expectStatus().isOk()
                .expectBody(Article.class)
                .value(Article::getId, equalTo(10L))
                .value(Article::getName, equalTo("someName"))
                .value(Article::getBody, equalTo("someBody"))
        ;
    }

    @Test
    @WithMockUser
    void findComments() {
        when(articleCommentRepository.findByArticleId(eq(1L)))
                .thenReturn(Flux.fromIterable(Arrays.asList(
                        new ArticleComment(1L, 1L, "name", "body"),
                        new ArticleComment(2L, 1L, "name", "body")
                )));

        WebTestClient.ResponseSpec result = client.get()
                .uri("/api/articles/1/comments")
                .exchange();

        result.expectStatus().isOk()
                .expectBodyList(ArticleComment.class)
                .hasSize(2)
        ;
        verify(articleCommentRepository).findByArticleId(eq(1L));
    }

    @Test
    @WithMockUser
    void createComment() {
        when(articleCommentRepository.save(any()))
                .thenReturn(Mono.just(new ArticleComment(20L, 2L, "someName", "someBody")));

        WebTestClient.ResponseSpec result = client.post()
                .uri("/api/articles/2/comments")
                .bodyValue(new NewArgumentComment(2L, "someName", "someBody"))
                .exchange();

        result.expectStatus().isCreated()
                .expectBody(ArticleComment.class)
                .value(ArticleComment::getId, equalTo(20L))
                .value(ArticleComment::getArticleId, equalTo(2L));
        ArgumentCaptor<ArticleComment> captor = ArgumentCaptor.forClass(ArticleComment.class);
        verify(articleCommentRepository).save(captor.capture());
        assertEquals(2L, captor.getValue().getArticleId());
        assertEquals("someName", captor.getValue().getName());
        assertEquals("someBody", captor.getValue().getBody());
    }
    @Test
    @WithMockUser
    void givenMismatchedArticleId_whenCreateComment_thenThrowException() {
        WebTestClient.ResponseSpec result = client.post()
                .uri("/api/articles/2/comments")
                .bodyValue(new NewArgumentComment(1L, "someName", "someBody"))
                .exchange();

        result.expectStatus().is5xxServerError();
    }

    @Test
    @WithMockUser
    void findCommentById() {
        when(articleCommentRepository.findById(eq(10L)))
                .thenReturn(Mono.just(new ArticleComment(10L, 2L, "someName", "someTitle")));

        WebTestClient.ResponseSpec result = client.get()
                .uri("/api/articles/2/comments/10")
                .exchange();

        result.expectStatus().isOk()
                .expectBody(ArticleComment.class)
                .value(ArticleComment::getId, equalTo(10L))
        ;
        verify(articleCommentRepository).findById(eq(10L));
    }
}