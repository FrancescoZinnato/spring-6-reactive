package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.model.CustomerDTO;
import guru.springframework.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String LOCALHOST = "http://localhost:8080";
    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer customerId) {
        return customerService.deleteCustomerById(customerId).then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> patchCustomer(@PathVariable Integer customerId, @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.patchCustomer(customerId, customerDTO).map(customer -> ResponseEntity.noContent().build());
    }

    @PutMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> updateCustomer(@PathVariable Integer customerId, @Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomer(customerId, customerDTO).map(customer -> ResponseEntity.noContent().build());
    }

    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<Void>> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        return customerService.saveNewCustomer(customerDTO).map(customer -> ResponseEntity.created(UriComponentsBuilder.fromUriString(LOCALHOST + CUSTOMER_PATH).build().toUri()).build());
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId);
    }

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> getAllCustomers() {
        return customerService.listCustomers();
    }

}
