package pl.com.psl.java.rx.demo.portfolioservice.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.com.psl.java.rx.demo.portfolioservice.model.Portfolio;
import reactor.core.publisher.Flux;

@Repository
public interface PortfolioRepository extends ReactiveCrudRepository<Portfolio, String> {

    Flux<Portfolio> findByCustomerId(Long customerId);
}
