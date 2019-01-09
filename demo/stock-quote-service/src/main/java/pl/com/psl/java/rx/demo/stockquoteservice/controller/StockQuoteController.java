package pl.com.psl.java.rx.demo.stockquoteservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.psl.java.rx.demo.stockquoteservice.model.StockQuotes;
import pl.com.psl.java.rx.demo.stockquoteservice.service.StockQuoteService;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
@RequestMapping("/stock-quotes")
public class StockQuoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockQuoteController.class);

    @Autowired
    private StockQuoteService stockQuoteService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockQuotes> getStockQuotesStream() {
        LOGGER.info("Received stock quotes request");
        Flux<StockQuotes> stockQuotes = stockQuoteService.getStockQuotes();
        LOGGER.info("Returning stock quotes flux response");
        return stockQuotes;
    }
}
