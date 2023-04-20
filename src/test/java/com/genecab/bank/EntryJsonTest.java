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
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class EntryJsonTest {

    @Autowired
    private JacksonTester<Entry> json;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void accountSerializationTest() throws IOException {
        Entry entry = new Entry(14L, "Withdrawal", LocalDateTime.of(2023, 4, 17, 8, 35, 3, 123456789), "-100.00", "500");
//		assertThat(json.write(entry)).isStrictlyEqualToJson("expected-entry.json");
        assertThat(json.write(entry)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(entry)).extractingJsonPathNumberValue("@.id").isEqualTo(14);
        assertThat(json.write(entry)).hasJsonPathStringValue("@.description");
        assertThat(json.write(entry)).extractingJsonPathStringValue("@.description").isEqualTo("Withdrawal");

        assertThat(json.write(entry)).hasJsonPathStringValue("@.amount");

        JsonContent<Entry> jsonContent = json.write(entry);
        String jsonContentStr = jsonContent.getJson();
        JsonNode node = objectMapper.readValue(jsonContentStr, JsonNode.class);
        JsonNode amountNode = node.get("amount");
        String amountStr = amountNode.textValue();
        BigDecimal amountBD = new BigDecimal(amountStr);
        BigDecimal comparingAmount = new BigDecimal("-100.00");
        assert amountBD.equals(comparingAmount);

        assertThat(json.write(entry)).hasJsonPathStringValue("@.balance");
        JsonNode balanceNode = node.get("balance");
        String balanceStr = balanceNode.textValue();
        BigDecimal balanceBD = new BigDecimal(balanceStr);
        BigDecimal comparingBalance = new BigDecimal("500");
        assert balanceBD.equals(comparingBalance);
    }

}
