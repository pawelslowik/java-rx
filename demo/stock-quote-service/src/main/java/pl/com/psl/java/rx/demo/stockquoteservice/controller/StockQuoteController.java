package pl.com.psl.java.rx.demo.stockquoteservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.psl.java.rx.demo.stockquoteservice.model.StockQuotes;
import pl.com.psl.java.rx.demo.stockquoteservice.service.StockQuoteService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stock-quotes")
public class StockQuoteController {

    @Autowired
    private StockQuoteService stockQuoteService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockQuotes> getStockQuotesStream() {
        return stockQuoteService.getStockQuotes();
    }
}
