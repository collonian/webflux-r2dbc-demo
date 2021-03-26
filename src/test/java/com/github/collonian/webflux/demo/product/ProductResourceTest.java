package com.github.collonian.webflux.demo.product;

import com.github.collonian.webflux.demo.config.DemoWebSecurityConfig;
import com.github.collonian.webflux.demo.product.vo.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ProductResource.class)
@Import(DemoWebSecurityConfig.class)
class ProductResourceTest {
    @Autowired
    private WebTestClient client;
    @MockBean
    private ProductRepository productRepository;

    @Test
    @WithMockUser
    public void findProducts() {
        when(productRepository.findAvailable(any()))
                .thenReturn(Flux.fromIterable(Arrays.asList(new Product())));

        WebTestClient.ResponseSpec result = client.get()
                .uri("/api/products")
                .exchange();

        result.expectStatus().isOk()
                .expectBodyList(Product.class)
                .hasSize(1);

        verify(productRepository).findAvailable(any());
    }
}