package com.github.collonian.webflux.demo.simple;

import com.github.collonian.webflux.demo.simple.vo.Simple;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/simple")
public class SimpleResource {
    private List<Simple> simples = Arrays.asList(
            new Simple(1, "first"),
            new Simple(2, "second"),
            new Simple(3, "third")
    );
    @GetMapping
    public Flux<Simple> find(SpringDataWebProperties.Pageable pageable) {
        return Flux.fromIterable(simples);
    }
    @GetMapping("/{id}")
    public Mono<Simple> findById(@PathVariable("id") int id) {
        return Flux.fromIterable(simples)
                .filter(s -> s.getId() == id)
                .next();
    }
}
