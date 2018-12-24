package pl.com.psl.java.rx.demo.customerservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {
    private Long id;
    private String name;
}
