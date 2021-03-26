package com.github.collonian.webflux.demo.config;

import com.github.collonian.webflux.demo.investment.exception.UnprocessableInvestmentException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class DemoWebExceptionAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);

        if (getError(request) instanceof UnprocessableInvestmentException) {
            UnprocessableInvestmentException exception = (UnprocessableInvestmentException) getError(request);
            map.put("error_code", exception.getErrorCode());
            return map;
        }
        return map;
    }
}
