package pl.com.psl.java.rx.demo.portfolioservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.com.psl.java.rx.demo.portfolioservice.model.ValuationReport;
import pl.com.psl.java.rx.demo.portfolioservice.service.PortfolioService;
import pl.com.psl.java.rx.demo.portfolioservice.model.Portfolio;
import pl.com.psl.java.rx.demo.portfolioservice.model.Valuation;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class PortfolioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping(value = "/portfolio/{customerId}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Portfolio> getCustomerPortfolio(@PathVariable Long customerId) {
        return portfolioService.getCustomerPortfolio(customerId);
    }

    @GetMapping(value = "/valuation/{customerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Valuation>> getCustomerPortfolioEvaluation(@PathVariable Long customerId) {
        return portfolioService.getCustomerPortfolioValuation(customerId);
    }

    @GetMapping(value = "/report/{customerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ValuationReport> getCustomerPortfolioValuationReport(@PathVariable Long customerId) {
        LOGGER.info("Requesting portfolio valuation report for customerId={}", customerId);
        Flux<ValuationReport> customerPortfolioValuationReport = portfolioService.getCustomerPortfolioValuationReport(customerId);
        LOGGER.info("Finished requesting portfolio valuation report for customerId={}", customerId);
        return customerPortfolioValuationReport;
    }
}
