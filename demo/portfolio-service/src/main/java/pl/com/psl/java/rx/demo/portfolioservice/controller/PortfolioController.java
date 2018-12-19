package pl.com.psl.java.rx.demo.portfolioservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.com.psl.java.rx.demo.portfolioservice.service.PortfolioService;
import pl.com.psl.java.rx.demo.portfolioservice.model.Portfolio;
import pl.com.psl.java.rx.demo.portfolioservice.model.Valuation;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping(value = "/portfolio/{customerName}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Portfolio> getCustomerPortfolio(@PathVariable String customerName) {
        return portfolioService.getCustomerPortfolio(customerName);
    }

    @GetMapping(value = "/valuation/{customerName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Valuation>> getCustomerPortfolioEvaluation(@PathVariable String customerName) {
        return portfolioService.getCustomerPortfolioValuation(customerName);
    }
}
