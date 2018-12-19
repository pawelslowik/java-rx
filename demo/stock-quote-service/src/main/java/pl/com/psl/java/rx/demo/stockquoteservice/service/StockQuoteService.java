package pl.com.psl.java.rx.demo.stockquoteservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.psl.java.rx.demo.stockquoteservice.model.StockQuote;
import pl.com.psl.java.rx.demo.stockquoteservice.model.StockQuotes;
import pl.com.psl.java.rx.demo.stockquoteservice.repository.StockQuoteRepository;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StockQuoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockQuoteService.class);

    @Autowired
    private StockQuoteRepository stockQuoteRepository;

    private Flux<StockQuotes> stockQuotes;

    @PostConstruct
    public void startPublishingStockQuotes() {
        Flux<List<StockQuote>> generatedStockQuotes = Flux.generate((SynchronousSink<List<StockQuote>> sink) -> {
            Flux.fromStream(Stream.of("stock_a", "stock_b", "stock_c"))
                    .map(stockSymbol -> {
                        LocalDateTime timestamp = LocalDateTime.now();
                        BigDecimal price = BigDecimal.valueOf(ThreadLocalRandom.current().nextLong(0, 10));
                        return new StockQuote(stockSymbol, price, timestamp);
                    })
                    .collect(Collectors.toList())
                    .subscribe(sink::next);
        });

        stockQuotes = Flux.interval(Duration.ofSeconds(5))
                .zipWith(generatedStockQuotes)
                .map(Tuple2::getT2)
                .flatMap(list -> Flux.fromIterable(list)
                        .flatMap(stockQuoteRepository::save)
                        .collectList()
                )
                .map(StockQuotes::new)
                .doOnNext(sq -> LOGGER.info("Publishing stock quotes={}", sq))
                .publish();

        ((ConnectableFlux<StockQuotes>) stockQuotes).connect();
    }

    public Flux<StockQuotes> getStockQuotes() {
        return stockQuotes;
    }
}


