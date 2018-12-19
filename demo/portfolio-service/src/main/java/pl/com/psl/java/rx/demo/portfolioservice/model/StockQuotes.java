package pl.com.psl.java.rx.demo.portfolioservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class StockQuotes {
    @NonNull
    private List<StockQuote> stockQuotes;
}
