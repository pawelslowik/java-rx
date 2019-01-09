package pl.com.psl.java.rx.demo.customerservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    private static final Map<Long, Customer> CUSTOMERS = Stream.of(1L,2L,3L)
            .collect(Collectors.toMap(Function.identity(), (Long id) -> new Customer(id, "customer " + id)));

    @GetMapping(value = "/customers/{id}")
    public Customer getCustomerSlowly(@PathVariable Long id) throws InterruptedException {
        LOGGER.info("Received customer(id={}) request", id);
        Thread.sleep(5000);
        Customer customer = CUSTOMERS.get(id);
        LOGGER.info("Returning customer(id={}) response", id);
        return customer;
    }

}
