package guru.springframework.spring6reactive.controllers;

import guru.springframework.spring6reactive.domain.Customer;
import guru.springframework.spring6reactive.model.CustomerDTO;
import guru.springframework.spring6reactive.repositories.CustomerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    WebTestClient webClient;

    @Test
    @Order(999)
    void testDeleteCustomer() {
        webClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(10)
    void testDeleteCustomerNotFound() {
        webClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(9)
    void testPatchIdNotFound() {
        webClient.patch().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(8)
    void testUpdateCustomerNotFound() {
        webClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(7)
    void testGetCustomerIdNotFound() {
        webClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(6)
    void testUpdateCustomerFail() {
        Customer customer = CustomerRepositoryTest.getTestCustomer();
        customer.setCustomerName("");

        webClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(customer), CustomerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(5)
    void testCreateCustomerFail() {
        Customer customer = CustomerRepositoryTest.getTestCustomer();
        customer.setCustomerName("");

        webClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(customer), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(4)
    void testUpdateCustomer() {
        webClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(3)
    void testCreateCustomer() {
        webClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/%7BcustomerId%7D");
    }

    @Test
    @Order(1)
    void testGetCustomerId() {
        webClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(2)
    void testGetCustomers() {
        webClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(2);
    }

}