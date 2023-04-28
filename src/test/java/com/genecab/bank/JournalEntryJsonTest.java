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

@ExtendWith(SpringExtension.class)
@JsonTest
public class JournalEntryJsonTest {

    @Autowired
    private JacksonTester<JournalEntry> json;

    @Autowired
    private ObjectMapper objectMapper;

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

}
