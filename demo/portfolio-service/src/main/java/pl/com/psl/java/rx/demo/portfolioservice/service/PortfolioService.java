package pl.com.psl.java.rx.demo.portfolioservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pl.com.psl.java.rx.demo.portfolioservice.model.Portfolio;
import pl.com.psl.java.rx.demo.portfolioservice.model.StockQuotes;
import pl.com.psl.java.rx.demo.portfolioservice.model.Valuation;
import pl.com.psl.java.rx.demo.portfolioservice.repository.PortfolioRepository;
import reactor.core.publisher.Flux;

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
                new Portfolio("bob", "stock_a", 10L),
                new Portfolio("bob", "stock_b", 5L),
                new Portfolio("tom", "stock_b", 1L)
                )
        ).publish().connect();
    }

    public Flux<Portfolio> getCustomerPortfolio(String customerName) {
        return portfolioRepository.findByCustomerName(customerName);
    }

    public Flux<List<Valuation>> getCustomerPortfolioValuation(String customerName) {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        Flux<StockQuotes> stockQuotes = webClient.get()
                .uri("/stock-quotes")
                .retrieve()
                .bodyToFlux(StockQuotes.class);

        Flux<Portfolio> customerPortfolio = getCustomerPortfolio(customerName);

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
                        .flux()
                );
    }
}
