package pl.com.psl.java.rx.demo.stockquoteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class StockQuoteServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockQuoteServiceApplication.class, args);
	}
}

