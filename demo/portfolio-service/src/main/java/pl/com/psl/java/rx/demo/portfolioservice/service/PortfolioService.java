package pl.com.psl.java.rx.demo.portfolioservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.psl.java.rx.demo.portfolioservice.model.*;
import pl.com.psl.java.rx.demo.portfolioservice.repository.PortfolioRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortfolioService.class);

    @Autowired
    private PortfolioRepository portfolioRepository;

    @PostConstruct
    public void initRepository() {
        portfolioRepository.saveAll(Arrays.asList(
                new Portfolio(1L, "stock_a", 10L),
                new Portfolio(1L, "stock_b", 5L),
                new Portfolio(2L, "stock_b", 1L)
                )
        ).subscribe();
    }

    public Flux<Portfolio> getCustomerPortfolio(Long customerId) {
        return portfolioRepository.findByCustomerId(customerId);
    }

    public Flux<List<Valuation>> getCustomerPortfolioValuation(Long customerId) {
        Flux<StockQuotes> stockQuotes = getStockQuotes();
        Flux<Portfolio> customerPortfolio = getCustomerPortfolio(customerId);

        return stockQuotes
                .flatMap(sqs -> customerPortfolio.collectList()
                        .map(portfolios -> portfolios.stream()
                                .map(portfolio -> {
                                    BigDecimal price = sqs.getStockQuotes().stream()
                                            .filter(sq -> sq.getSymbol().equals(portfolio.getStockSymbol()))
                                            .findFirst()
                                            .get()
                                            .getPrice();
                                    return new Valuation(portfolio.getStockSymbol(), price.multiply(BigDecimal.valueOf(portfolio.getStockAmount())));
                                })
                                .collect(Collectors.toList())
                        )
                );
    }

    public Flux<ValuationReport> getCustomerPortfolioValuationReport(Long customerId) {
        return  getCustomerPortfolioValuation(customerId)
                .map(valuation -> valuation.stream().map(Valuation::getValue).reduce(BigDecimal.ZERO, BigDecimal::add))
                .doOnNext(val -> LOGGER.info("Calculated new total value"))
                .flatMap(totalValue -> Mono.fromCallable(() -> getCustomer(customerId))
                        .doOnNext(customer -> LOGGER.info("Received customer data"))
                        .map(customer -> new ValuationReport(customer.getName(), totalValue))
                )
                .doOnNext(valuationReport -> LOGGER.info("Generated new valuation report"));
    }

    private Flux<StockQuotes> getStockQuotes() {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        return  webClient.get()
                .uri("/stock-quotes")
                .retrieve()
                .bodyToFlux(StockQuotes.class)
                .doOnRequest(amount -> LOGGER.info("Requesting {} stock quotes", amount))
                .doOnNext(sq -> LOGGER.info("Received new stock quotes"));
    }

    private Customer getCustomer(Long customerId) {
        LOGGER.info("Getting customer by id={}", customerId);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://localhost:8082/customers/" + customerId, Customer.class);
    }
}
