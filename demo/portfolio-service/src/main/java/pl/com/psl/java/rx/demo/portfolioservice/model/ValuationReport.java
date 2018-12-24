package pl.com.psl.java.rx.demo.portfolioservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ValuationReport {
    private String customerName;
    private BigDecimal totalValue;
}
