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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void shouldReturnAnAccountWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts/1001", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1001);
        assertThat(id).isNotNull();
    }

    @Test
    void shouldNotReturnAnAccountWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts/123453429870932", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldReturnAJournalEntryWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/journal-entries/13893", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(13893);
        assertThat(id).isNotNull();
    }

    @Test
    void shouldNotReturnAJournalEntryWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/journal-entries/123453429870932", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }

    @Test
    @DirtiesContext
    void shouldCreateANewAccount() {
        Account newAccount = new Account(123446L, "John Savings", "SAVINGS", null);
        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .postForEntity("/accounts", newAccount, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewAccount = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity(locationOfNewAccount, String.class);
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
        JournalEntry newJournalEntry = new JournalEntry(3240987L, 1002L, "Deposit", 1682675287000L, "150.00", null);
        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .postForEntity("/journal-entries", newJournalEntry, Void.class);
        System.out.println("Status code:" + createResponse.getStatusCode());
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewAccount = createResponse.getHeaders().getLocation();
        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity(locationOfNewAccount, String.class);
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
        assertThat(account).isEqualTo(1002);
        assertThat(description.trim()).isEqualTo("Deposit");
        assertThat(instant).isEqualTo(1682675287000L);
        assertThat(amount.trim()).isEqualTo("150.00");
        assertThat(balance).isEqualTo("336.00");
    }

    @Test
    @DirtiesContext
    void shouldReturnBadRequestIfPrincipalDoesNotHaveAccessToAccountWhenCreatingJournalEntry() {
        JournalEntry newJournalEntry = new JournalEntry(null, 1004L, "Deposit", 1682675287000L, "150.00", null);
        ResponseEntity<Void> createResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .postForEntity("/journal-entries", newJournalEntry, Void.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnAllAccountsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int cashCardCount = documentContext.read("$.length()");
        assertThat(cashCardCount).isEqualTo(6);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(123446, 1001, 1002, 1003, 1005, 1006);

        JSONArray names = documentContext.read("$..name");
        assertThat(names).containsExactlyInAnyOrder(
                "Jeremy                                                                                              ",
                "John Savings                                                                                        ",
                "Emma                                                                                                ",
                "Sally                                                                                               ",
                "Emma                                                                                                ",
                "Emma                                                                                                "
        );

        JSONArray types = documentContext.read("$..type");
        assertThat(types).containsExactlyInAnyOrder(
                "SAVINGS                                           ",
                "SAVINGS                                           ",
                "SAVINGS                                           ",
                "MONEY_MARKET                                      ",
                "CHECKING                                          ",
                "SAVINGS                                           ");
    }

    @Test
    void shouldReturnAllJournalEntriesWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/journal-entries", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int cashCardCount = documentContext.read("$.length()");
        assertThat(cashCardCount).isEqualTo(7);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(13892, 13893, 13894, 13895, 13896, 13897, 3240987);

        JSONArray descriptions = documentContext.read("$..description");
        assertThat(descriptions).containsExactlyInAnyOrder(
                "Beginning Balance                                                                                   ",
                "Beginning Balance                                                                                   ",
                "Deposit                                                                                             ",
                "Withdrawal                                                                                          ",
                "Deposit                                                                                             ",
                "Interest payment                                                                                    ",
                "Deposit                                                                                             "
                );


        JSONArray balances = documentContext.read("$..balance");
        assertThat(balances).containsExactlyInAnyOrder("100.00              ",
                "0.00                ",
                "125.00              ",
                "105.00              ",
                "185.00              ",
                "186.00              ",
                "336.00              ");
    }

    @Test
    void shouldReturnAPageOfAccounts() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnAPageOfJournalEntries() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/journal-entries?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnASortedPageOfAccounts() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts?page=0&size=1&sort=name,type", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        String name = documentContext.read("$[0].name");
        assertThat(name).isEqualTo("Emma                                                                                                ");

        String type = documentContext.read("$[0].type");
        assertThat(type).isEqualTo("CHECKING                                          ");
    }

    @Test
    void shouldReturnASortedPageOfJournalEntries() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/journal-entries?page=0&size=1&sort=id,desc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        int id = documentContext.read("$[0].id");
        assertThat(id).isEqualTo(3240987);
    }

    @Test
    void shouldReturnASortedPageOfAccountsWithNoParametersAndUseDefaultValues() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(6);

        JSONArray names = documentContext.read("$..name");
        assertThat(names).containsExactly("Emma                                                                                                ",
                "Emma                                                                                                ",
                "Emma                                                                                                ",
                "Jeremy                                                                                              ",
                "John Savings                                                                                        ",
                "Sally                                                                                               ");
    }

    @Test
    void shouldReturnASortedPageOfJournalEntriesWithNoParametersAndUseDefaultValues() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/journal-entries", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(7);

        JSONArray id = documentContext.read("$..id");
        assertThat(id).containsExactly(3240987, 13897, 13896,13895,13894,13893,13892);
    }
    @Test
    void shouldNotReturnAnAccountWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("BAD-USER", "abc123")
                .getForEntity("/accounts/1001", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate
                .withBasicAuth("sarah1", "BAD-PASSWORD")
                .getForEntity("/accounts/1001", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldRejectUsersWhoAreNotCardOwners() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("hank-owns-no-cards", "qrs456")
                .getForEntity("/accounts/1001", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldNotAllowAccessToAccountsTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts/1004", String.class); // kumar2's data
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotAllowAccessToASingleJournalEntryTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/journal-entries/13892", String.class); // kumar2's data
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void shouldUpdateAnExistingAccount() {
        Account accountUpdate = new Account(1001L, "Jeremy", "CHECKING", "sarah1");
        HttpEntity<Account> request = new HttpEntity<>(accountUpdate);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .exchange("/accounts/1001", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getResponse = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/accounts/1001", String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String type = documentContext.read("$.type");
        assertThat(id).isEqualTo(1001);
        assertThat(type).isEqualTo("CHECKING                                          ");
    }

    @Test
    void shouldNotUpdateAnAccountThatDoesNotExist() {
        Account accountUpdate = new Account(1001345L, "Jeremy", "CHECKING", "sarah1");
        HttpEntity<Account> request = new HttpEntity<>(accountUpdate);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .exchange("/accounts/99999345", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotUpdateAnAccountThatIsOwnedBySomeoneElse() {
        Account accountUpdate = new Account(1004L, "Jeremy", "CHECKING", "sarah1");
        HttpEntity<Account> request = new HttpEntity<>(accountUpdate);
        ResponseEntity<Void> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .exchange("/accounts/1004", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
