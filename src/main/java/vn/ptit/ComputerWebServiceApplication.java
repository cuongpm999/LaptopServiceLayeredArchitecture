package vn.ptit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.ptit.entities.elasticsearch.ECash;
import vn.ptit.repositories.elasticsearch.ECashRepository;

@SpringBootApplication
public class ComputerWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComputerWebServiceApplication.class, args);
    }

}
