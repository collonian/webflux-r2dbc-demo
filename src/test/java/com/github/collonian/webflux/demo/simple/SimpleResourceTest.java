package com.github.collonian.webflux.demo.simple;

import com.github.collonian.webflux.demo.config.DemoWebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = SimpleResource.class)
@Import(DemoWebSecurityConfig.class)
class SimpleResourceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void find() {
        this.webTestClient.get().uri("/api/simple")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].name").isEqualTo("second")
        ;
    }

    @Test
    void findById() {
        this.webTestClient.get().uri("/api/simple/3")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.name").isEqualTo("third")
        ;
    }
}