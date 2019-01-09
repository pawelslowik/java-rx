package pl.com.psl.java.rx.demo.portfolioservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.com.psl.java.rx.demo.portfolioservice.model.Portfolio;
import pl.com.psl.java.rx.demo.portfolioservice.model.Valuation;
import pl.com.psl.java.rx.demo.portfolioservice.model.ValuationReport;
import pl.com.psl.java.rx.demo.portfolioservice.service.PortfolioService;
import reactor.core.publisher.Flux;

import java.util.List;

@CrossOrigin
@RestController
public class PortfolioController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping(value = "/portfolio/{customerId}")
    public Flux<Portfolio> getCustomerPortfolio(@PathVariable Long customerId) {
        LOGGER.info("Received customer(id={}) portfolio request", customerId);
        Flux<Portfolio> customerPortfolio = portfolioService.getCustomerPortfolio(customerId)
                .doOnComplete(() -> LOGGER.info("Returning customer(id={}) portfolio", customerId));
        LOGGER.info("Returning customer(id={}) portfolio flux response", customerId);
        return customerPortfolio;
    }

    @GetMapping(value = "/valuation/{customerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Valuation>> getCustomerPortfolioEvaluation(@PathVariable Long customerId) {
        LOGGER.info("Received customer(id={}) portfolio valuation request", customerId);
        Flux<List<Valuation>> customerPortfolioValuation = portfolioService.getCustomerPortfolioValuation(customerId);
        LOGGER.info("Returning customer(id={}) portfolio valuation flux response", customerId);
        return customerPortfolioValuation;
    }

    @GetMapping(value = "/report/{customerId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ValuationReport> getCustomerPortfolioValuationReport(@PathVariable Long customerId) {
        LOGGER.info("Received customer(id={}) portfolio report request", customerId);
        Flux<ValuationReport> customerPortfolioValuationReport = portfolioService.getCustomerPortfolioValuationReport(customerId);
        LOGGER.info("Returning customer(id={}) portfolio report flux response", customerId);
        return customerPortfolioValuationReport;
    }
}
