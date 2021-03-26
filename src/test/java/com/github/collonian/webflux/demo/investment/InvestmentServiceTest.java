package com.github.collonian.webflux.demo.investment;

import com.github.collonian.webflux.demo.config.DemoR2DbcConfig;
import com.github.collonian.webflux.demo.investment.exception.UnprocessableInvestmentException;
import com.github.collonian.webflux.demo.investment.vo.Investment;
import com.github.collonian.webflux.demo.investment.vo.InvestmentEvent;
import com.github.collonian.webflux.demo.investment.vo.NewInvestmentParam;
import com.github.collonian.webflux.demo.product.ProductRepository;
import com.github.collonian.webflux.demo.product.vo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class InvestmentServiceTest {
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private InvestmentRepository investmentRepository;

    private InvestmentService investmentService;
    @BeforeEach
    public void beforeEach() {
        investmentService = new InvestmentService(productRepository, investmentRepository);

        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(Product.builder().id(1L).build()));
        when(investmentRepository.markInvestmentEvent(any()))
                .thenReturn(Mono.just(1));
        when(investmentRepository.isInvestmentAccepted(any()))
                .thenReturn(Mono.just(true));
    }

    @Test
    void givenInvalidProduct_tryInvestment_thenThrowInvalidProduct() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.empty());

        InvestmentEvent event = InvestmentEvent.from(new NewInvestmentParam(20L, 100L));
        Mono<Boolean> step = investmentService.tryInvestment(event);

        StepVerifier.create(step)
                .expectErrorMatches((e) -> ((UnprocessableInvestmentException)e)
                        .getErrorCode().equals(InvestmentError.INVALID_PROUDCT))
                .verify();
    }
    @Test
    void givenNotStartedProduct_tryInvestment_thenThrowNotStarted() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(
                        Product.builder()
                                .id(20L)
                                .startedAt(LocalDateTime.now().plusDays(5))
                                .finishedAt(LocalDateTime.now().plusDays(10))
                                .totalAmount(10000L)
                                .investmentAmount(0L)
                                .build()
                ));

        InvestmentEvent event = InvestmentEvent.from(new NewInvestmentParam(20L, 100L));
        Mono<Boolean> step = investmentService.tryInvestment(event);

        StepVerifier.create(step)
                .expectErrorMatches((e) -> ((UnprocessableInvestmentException)e)
                        .getErrorCode().equals(InvestmentError.NOT_STARTED))
                .verify();
    }

    @Test
    void givenFinishedProduct_tryInvestment_thenThrowFinished() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(
                        Product.builder()
                                .id(20L)
                                .startedAt(LocalDateTime.now().minusDays(10))
                                .finishedAt(LocalDateTime.now().minusDays(5))
                                .totalAmount(10000L)
                                .investmentAmount(0L)
                                .build()
                ));

        InvestmentEvent event = InvestmentEvent.from(new NewInvestmentParam(20L, 100L));
        Mono<Boolean> step = investmentService.tryInvestment(event);

        StepVerifier.create(step)
                .expectErrorMatches((e) -> ((UnprocessableInvestmentException)e)
                        .getErrorCode().equals(InvestmentError.FINISHED))
                .verify();
    }

    @Test
    void givenSoldoutProduct_tryInvestment_thenThrowSoldout() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(
                        Product.builder()
                                .id(20L)
                                .startedAt(LocalDateTime.now().minusDays(10))
                                .finishedAt(LocalDateTime.now().plusDays(5))
                                .totalAmount(10000L)
                                .investmentAmount(10000L)
                                .build()
                ));

        InvestmentEvent event = InvestmentEvent.from(new NewInvestmentParam(20L, 100L));
        Mono<Boolean> step = investmentService.tryInvestment(event);

        StepVerifier.create(step)
                .expectErrorMatches((e) -> ((UnprocessableInvestmentException)e)
                        .getErrorCode().equals(InvestmentError.SOLDOUT))
                .verify();
    }

    @Test
    void givenNormalProductAndInvestmentAccepted_tryInvestment_thenSucceed() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(
                        Product.builder()
                                .id(20L)
                                .startedAt(LocalDateTime.now().minusDays(10))
                                .finishedAt(LocalDateTime.now().plusDays(5))
                                .totalAmount(10000L)
                                .investmentAmount(100L)
                                .build()
                ));

        InvestmentEvent event = InvestmentEvent.from(new NewInvestmentParam(20L, 100L));
        Mono<Boolean> step = investmentService.tryInvestment(event);

        StepVerifier.create(step)
                .expectNext(true)
                .verifyComplete();
        verify(productRepository).findById(eq(20L));
        ArgumentCaptor<InvestmentEvent> captor = ArgumentCaptor.forClass(InvestmentEvent.class);
        verify(investmentRepository).markInvestmentEvent(captor.capture());
        verify(investmentRepository).isInvestmentAccepted(eq(captor.getValue()));
    }

    @Test
    void givenNormalProductAndInvestmentUnacceptable_tryInvestment_thenMarkInvert() {
        when(productRepository.findById(any(Long.class)))
                .thenReturn(Mono.just(
                        Product.builder()
                                .id(20L)
                                .startedAt(LocalDateTime.now().minusDays(10))
                                .finishedAt(LocalDateTime.now().plusDays(5))
                                .totalAmount(500L)
                                .investmentAmount(450L)
                                .build()
                ));
        when(investmentRepository.isInvestmentAccepted(any()))
                .thenReturn(Mono.just(false));

        InvestmentEvent event = InvestmentEvent.from(new NewInvestmentParam(20L, 100L));
        Mono<Boolean> step = investmentService.tryInvestment(event);

        StepVerifier.create(step)
                .expectNext(false)
                .verifyComplete();
        verify(productRepository).findById(eq(20L));
        ArgumentCaptor<InvestmentEvent> captor = ArgumentCaptor.forClass(InvestmentEvent.class);
        verify(investmentRepository, times(2)).markInvestmentEvent(captor.capture());
        verify(investmentRepository).isInvestmentAccepted(eq(captor.getAllValues().get(0)));
        assertEquals(100L, captor.getAllValues().get(0).getAmount());
        assertEquals(-100L, captor.getAllValues().get(1).getAmount());
    }

    @Test
    void markInvestment() {
        when(investmentRepository.markInvestment(any()))
                .thenReturn(Mono.just(1));
        when(investmentRepository.findById(any()))
                .thenReturn(Mono.just(Investment.builder()
                        .id("some-uuid")
                        .build()));

        Investment investment = Investment.builder().id("some-uuid").build();
        investmentService.markInvestment(investment).block();

        verify(investmentRepository).markInvestment(eq(investment));
        verify(investmentRepository).findById(eq("some-uuid"));
    }
}