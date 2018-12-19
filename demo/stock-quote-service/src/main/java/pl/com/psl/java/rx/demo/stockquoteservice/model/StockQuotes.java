package pl.com.psl.java.rx.demo.stockquoteservice.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class StockQuotes {
    @NonNull
    private List<StockQuote> stockQuotes;
}
