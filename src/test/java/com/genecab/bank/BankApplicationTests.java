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
import java.util.Optional;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
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
    @DirtiesContext
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
    @DirtiesContext
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

    @Test
    @DirtiesContext
    void shouldCreateANewJournalEntry() {
        JournalEntry newJournalEntry = new JournalEntry(null, 1001L, "Deposit", 1682675287000L, "150.00", null);
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/journal-entries", newJournalEntry, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewAccount = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewAccount, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        Number account = documentContext.read("$.account");
        String description = documentContext.read("$.description");
        Number instant = documentContext.read("$.instant");
        String amount = documentContext.read("$.amount");
        Optional<String> balanceOpt = Optional.of(documentContext.read("$.balance"));
        String balance = Optional.ofNullable(balanceOpt).map(Optional::get).orElse("").trim();

        assertThat(id).isNotNull();
        assertThat(account).isEqualTo(1001);
        assertThat(description.trim()).isEqualTo("Deposit");
        assertThat(instant).isEqualTo(1682675287000L);
        assertThat(amount.trim()).isEqualTo("150.00");
        assertThat(balance).isEqualTo("250.00");
    }

    @Test
    void shouldReturnAllAccountsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int cashCardCount = documentContext.read("$.length()");
        assertThat(cashCardCount).isEqualTo(6);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(1001, 1002, 1003, 1004, 1005, 1006);

        JSONArray names = documentContext.read("$..name");
        assertThat(names).containsExactlyInAnyOrder(
                "Jeremy                                                                                              ",
                "Emma                                                                                                ",
                "Sally                                                                                               ",
                "John                                                                                                ",
                "Jeremy                                                                                              ",
                "Jeremy                                                                                              "
        );

        JSONArray types = documentContext.read("$..type");
        assertThat(types).containsExactlyInAnyOrder(
                "SAVINGS                                           ",
                "SAVINGS                                           ",
                "SAVINGS                                           ",
                "SAVINGS                                           ",
                "MONEY_MARKET                                      ",
                "CHECKING                                          ");
    }

    @Test
    void shouldReturnAllJournalEntriesWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/journal-entries", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int cashCardCount = documentContext.read("$.length()");
        assertThat(cashCardCount).isEqualTo(6);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(13892, 13893, 13894, 13895, 13896, 13897);

        JSONArray descriptions = documentContext.read("$..description");
        assertThat(descriptions).containsExactlyInAnyOrder(
                "Beginning Balance                                                                                   ",
                "Beginning Balance                                                                                   ",
                "Deposit                                                                                             ",
                "Withdrawal                                                                                          ",
                "Deposit                                                                                             ",
                "Interest payment                                                                                    ");


        JSONArray balances = documentContext.read("$..balance");
        assertThat(balances).containsExactlyInAnyOrder("100.00              ",
                "0.00                ",
                "125.00              ",
                "105.00              ",
                "185.00              ",
                "186.00              ");
    }

    @Test
    void shouldReturnAPageOfAccounts() {
        ResponseEntity<String> response = restTemplate.getForEntity("/accounts?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }
}
