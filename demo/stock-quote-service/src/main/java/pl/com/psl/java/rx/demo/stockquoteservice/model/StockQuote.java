package pl.com.psl.java.rx.demo.stockquoteservice.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document
public class StockQuote {
    @Id
    private String id;
    @NonNull
    private String symbol;
    @NonNull
    private BigDecimal price;
    @NonNull
    private LocalDateTime timestamp;
}
