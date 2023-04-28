package com.genecab.bank;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAnAccountWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts/1001", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1001);
        assertThat(id).isNotNull();
    }

    @Test
    void shouldNotReturnAnAccountWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts/123453429870932", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldReturnAJournalEntryWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/journal-entries/13892", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(13892);
        assertThat(id).isNotNull();
    }

    @Test
    void shouldNotReturnAJournalEntryWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/journal-entries/123453429870932", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    void shouldCreateANewAccount() {
        Account newAccount = new Account(null, "John Savings", "SAVINGS");
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/accounts", newAccount, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewAccount = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewAccount, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String name = documentContext.read("$.name");
        String type = documentContext.read("$.type");

        assertThat(id).isNotNull();
        assertThat(name.trim()).isEqualTo("John Savings");
        assertThat(type.trim()).isEqualTo("SAVINGS");
    }
}
