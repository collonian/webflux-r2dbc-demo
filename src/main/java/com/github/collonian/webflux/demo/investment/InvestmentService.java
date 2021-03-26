package com.github.collonian.webflux.demo.investment;

import com.github.collonian.webflux.demo.investment.exception.UnprocessableInvestmentException;
import com.github.collonian.webflux.demo.investment.vo.Investment;
import com.github.collonian.webflux.demo.investment.vo.InvestmentEvent;
import com.github.collonian.webflux.demo.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class InvestmentService {
    private final ProductRepository productRepository;
    private final InvestmentRepository investmentRepository;

    public InvestmentService(ProductRepository productRepository, InvestmentRepository investmentRepository) {
        this.productRepository = productRepository;
        this.investmentRepository = investmentRepository;
    }

    @Transactional(noRollbackFor = Exception.class)
    public Mono<Boolean> tryInvestment(InvestmentEvent event) {
        return productRepository
                .findById(event.getProductId())
                .switchIfEmpty(Mono.error(new UnprocessableInvestmentException(InvestmentError.INVALID_PROUDCT)))
                .handle((product, sink) -> {
                    if(LocalDateTime.now().isBefore(product.getStartedAt())) {
                        sink.error(new UnprocessableInvestmentException(InvestmentError.NOT_STARTED));
                    } else if(product.getInvestmentAmount() >= product.getTotalAmount()) {
                        sink.error(new UnprocessableInvestmentException(InvestmentError.SOLDOUT));
                    } else if(LocalDateTime.now().isAfter(product.getFinishedAt())) {
                        sink.error(new UnprocessableInvestmentException(InvestmentError.FINISHED));
                    } else {
                        sink.next(product);
                    }
                })
                .then(investmentRepository.markInvestmentEvent(event))
                .then(investmentRepository.isInvestmentAccepted(event))
                .flatMap(accepted -> {
                    if(!accepted) {
                        return investmentRepository
                                .markInvestmentEvent(InvestmentEvent.invert(event))
                                .then(Mono.just(false));
                    }
                    return Mono.just(true);
                });
    }
    public Mono<Investment> markInvestment(Investment investment) {
        return investmentRepository.markInvestment(investment)
                .then(investmentRepository.findById(investment.getId()))
                ;
    }
}
