package pl.com.psl.java.rx.demo.portfolioservice.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Portfolio {
    @Id
    private String id;
    @NonNull
    private Long customerId;
    @NonNull
    private String stockSymbol;
    @NonNull
    private Long stockAmount;
}
