package pl.com.psl.java.rx.demo.customerservice;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class CustomerController {

    private static final Map<Long, Customer> CUSTOMERS = Stream.of(1L,2L,3L)
            .collect(Collectors.toMap(Function.identity(), (Long id) -> new Customer(id, "customer " + id)));

    @GetMapping(value = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Customer getCustomerSlowly(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(5000);
        return CUSTOMERS.get(id);
    }

}
