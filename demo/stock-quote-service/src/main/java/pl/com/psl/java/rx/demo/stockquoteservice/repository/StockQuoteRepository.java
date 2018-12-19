package pl.com.psl.java.rx.demo.stockquoteservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.com.psl.java.rx.demo.stockquoteservice.model.StockQuote;

@Repository
public interface StockQuoteRepository extends ReactiveCrudRepository<StockQuote, String> {
}
