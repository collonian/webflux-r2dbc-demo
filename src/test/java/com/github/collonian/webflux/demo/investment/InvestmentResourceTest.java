package com.github.collonian.webflux.demo.investment;

import com.github.collonian.webflux.demo.config.DemoWebExceptionAttributes;
import com.github.collonian.webflux.demo.config.DemoWebSecurityConfig;
import com.github.collonian.webflux.demo.investment.vo.Investment;
import com.github.collonian.webflux.demo.investment.vo.NewInvestmentParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = InvestmentResource.class)
@Import({DemoWebSecurityConfig.class, DemoWebExceptionAttributes.class})
class InvestmentResourceTest {
    @Autowired
    private WebTestClient client;
    @MockBean
    private InvestmentService investmentService;

    @Test
    @WithMockUser
    void givenSuccessfulInvestment_tryInvestment_thenCreatedWithLocation() {
        when(investmentService.tryInvestment(any()))
                .thenReturn(Mono.just(true));
        when(investmentService.markInvestment(any()))
                .thenReturn(Mono.just(Investment.builder()
                        .id("some-id")
                        .build()
                ));

        WebTestClient.ResponseSpec result = client.post()
                .uri("/api/investments")
                .bodyValue(new NewInvestmentParam(10L, 255L))
                .exchange();

        result.expectStatus().isCreated()
                .expectHeader()
                .value("Location", equalTo("/api/investments/some-id"))
                .expectBody(Investment.class)
                .value(Investment::getId, equalTo("some-id"));
    }
    @Test
    @WithMockUser
    void givenUnacceptableInvestment_tryInvestment_thenUnprocessableWithExceedLimit() {
        when(investmentService.tryInvestment(any()))
                .thenReturn(Mono.just(false));

        WebTestClient.ResponseSpec result = client.post()
                .uri("/api/investments")
                .bodyValue(new NewInvestmentParam(10L, 255L))
                .exchange();

        result.expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.error_code").isEqualTo("EXCEED_LIMIT")
                .jsonPath("$.status").isEqualTo("422")
        ;

    }
}