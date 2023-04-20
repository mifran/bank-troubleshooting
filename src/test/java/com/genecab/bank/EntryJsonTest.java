package com.genecab.bank;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class EntryJsonTest {

	@Autowired
	private JacksonTester<Entry> json;

	@Test
	public void accountSerializationTest() throws IOException {
		Entry entry = new Entry(14L, "Withdrawal", LocalDateTime.of(2023, 4, 17,8,35,03,123456789), "-100.00", "500.00");
//		assertThat(json.write(entry)).isStrictlyEqualToJson("expected-entry.json");
		assertThat(json.write(entry)).hasJsonPathNumberValue("@.id");
		assertThat(json.write(entry)).extractingJsonPathNumberValue("@.id").isEqualTo(14);
		assertThat(json.write(entry)).hasJsonPathStringValue("@.description");
		assertThat(json.write(entry)).extractingJsonPathStringValue("@.description").isEqualTo("Withdrawal");
		assertThat(json.write(entry)).hasJsonPathStringValue("@.amount");
		assertThat(json.write(entry)).extractingJsonPathStringValue("@.amount").isEqualTo();
		assertThat(json.write(entry)).hasJsonPathStringValue("@.balance");
		assertThat(json.write(entry)).extractingJsonPathStringValue("@.balance").isEqualTo(500);
	}

}
