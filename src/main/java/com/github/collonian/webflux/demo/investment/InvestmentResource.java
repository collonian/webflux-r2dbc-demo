package com.github.collonian.webflux.demo.investment;

import com.github.collonian.webflux.demo.investment.exception.UnprocessableInvestmentException;
import com.github.collonian.webflux.demo.investment.vo.Investment;
import com.github.collonian.webflux.demo.investment.vo.InvestmentEvent;
import com.github.collonian.webflux.demo.investment.vo.NewInvestmentParam;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/investments")
public class InvestmentResource {
    private final InvestmentService investmentService;

    public InvestmentResource(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    @Transactional(noRollbackFor = Exception.class)
    public Mono<ResponseEntity<Investment>> tryInvestment(@RequestBody NewInvestmentParam param, UriComponentsBuilder uriBuilder) {
        InvestmentEvent event = InvestmentEvent.from(param);
        return investmentService.tryInvestment(event)
                .flatMap(accepted -> {
                    if(accepted) {
                        return investmentService.markInvestment(Investment.from(event))
                                .map(i -> {
                                    URI location = uriBuilder.path("/api/investments/{id}").build(i.getId());
                                    return ResponseEntity.created(location).body(i);
                                });
                    } else {
                        throw new UnprocessableInvestmentException(InvestmentError.EXCEED_LIMIT);
                    }
                });
    }
}
