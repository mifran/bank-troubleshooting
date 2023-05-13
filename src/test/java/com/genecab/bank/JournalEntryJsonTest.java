package com.genecab.bank;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(SpringExtension.class)
@JsonTest
public class JournalEntryJsonTest {

    @Autowired
    private JacksonTester<JournalEntry> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonTester<JournalEntry[]> jsonList;

    private JournalEntry[] journalEntries;

    @BeforeEach
    void setUp() {
        journalEntries = Arrays.array(
                new JournalEntry(13892L,1001L, "Beginning Balance", 16765L, "0.0", "100.0"),
                new JournalEntry(13893L,1002L, "Beginning Balance", 16765L, "0.0", "0.0"),
                new JournalEntry(13894L,1002L, "Deposit", 16765L, "125.0", "125.0"),
                new JournalEntry(13895L,1002L, "Withdrawal", 16765L, "-20.0", "105.0"),
                new JournalEntry(13896L,1002L, "Deposit", 16765L, "80.0", "185.0"),
                new JournalEntry(13897L,1002L, "Interest payment", 16765L, "1.0", "186.0"));
    }
    @Test
    public void journalEntrySerializationTest() throws IOException {

        JournalEntry journalEntry = new JournalEntry(14L,1001L, "Withdrawal", Instant.now().toEpochMilli(), "-100.00", "500");
        assertThat(json.write(journalEntry)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(journalEntry)).extractingJsonPathNumberValue("@.id").isEqualTo(14);
        assertThat(json.write(journalEntry)).hasJsonPathStringValue("@.description");
        assertThat(json.write(journalEntry)).extractingJsonPathStringValue("@.description").isEqualTo("Withdrawal");

        assertThat(json.write(journalEntry)).hasJsonPathStringValue("@.amount");

        JsonContent<JournalEntry> jsonContent = json.write(journalEntry);
        String jsonContentStr = jsonContent.getJson();
        JsonNode node = objectMapper.readValue(jsonContentStr, JsonNode.class);
        JsonNode amountNode = node.get("amount");
        String amountStr = amountNode.textValue();
        BigDecimal amountBD = new BigDecimal(amountStr);
        BigDecimal comparingAmount = new BigDecimal("-100.00");
        assert amountBD.equals(comparingAmount);

        assertThat(json.write(journalEntry)).hasJsonPathStringValue("@.balance");
        JsonNode balanceNode = node.get("balance");
        String balanceStr = balanceNode.textValue();
        BigDecimal balanceBD = new BigDecimal(balanceStr);
        BigDecimal comparingBalance = new BigDecimal("500");
        assert balanceBD.equals(comparingBalance);
    }

    @Test
    void journalEntryListSerializationTest() throws IOException {
        assertThat(jsonList.write(journalEntries)).isStrictlyEqualToJson("list-journal-entry.json");
    }

    @Test
    void journalEntryListDeserializationTest() throws IOException {
        String expected="""
            [
                {
                    "id": 13892,
                    "account": 1001,
                    "description": "Beginning Balance",
                    "instant": 16765,
                    "amount": "0.0",
                    "balance": "100.0"
                },
                {
                    "id": 13893,
                    "account": 1002,
                    "description": "Beginning Balance",
                    "instant": 16765,
                    "amount": "0.0",
                    "balance": "0.0"
                },
                {
                    "id": 13894,
                    "account": 1002,
                    "description": "Deposit",
                    "instant": 16765,
                    "amount": "125.0",
                    "balance": "125.0"
                },
                {
                    "id": 13895,
                    "account": 1002,
                    "description": "Withdrawal",
                    "instant": 16765,
                    "amount": "-20.0",
                    "balance": "105.0"
                },
                {
                    "id": 13896,
                    "account": 1002,
                    "description": "Deposit",
                    "instant": 16765,
                    "amount": "80.0",
                    "balance": "185.0"
                },
                {
                    "id": 13897,
                    "account": 1002,
                    "description": "Interest payment",
                    "instant": 16765,
                    "amount": "1.0",
                    "balance": "186.0"
                }
            ]
            """;
        assertThat(jsonList.parse(expected)).isEqualTo(journalEntries);
    }
}
