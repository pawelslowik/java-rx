package pl.com.psl.java.rx.demo.portfolioservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StockQuote {
    @NonNull
    private String symbol;
    @NonNull
    private BigDecimal price;
    @NonNull
    private LocalDateTime timestamp;
}
