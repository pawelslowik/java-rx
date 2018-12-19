package pl.com.psl.java.rx.demo.portfolioservice.model;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class Valuation {
    @NonNull
    private String stockSymbol;
    @NonNull
    private BigDecimal value;
}
