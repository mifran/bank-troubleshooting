package com.genecab.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class AccountJsonTest {

	@Autowired
	private JacksonTester<Account> json;

	@Test
	public void accountSerializationTest() throws IOException {
		Account account = new Account(1001L, "Aiden", AccountType.SAVINGS);
		assertThat(json.write(account)).isStrictlyEqualToJson("expected-account.json");
		assertThat(json.write(account)).hasJsonPathNumberValue("@.id");
		assertThat(json.write(account)).extractingJsonPathNumberValue("@.id").isEqualTo(1001);
		assertThat(json.write(account)).hasJsonPathStringValue("@.name");
		assertThat(json.write(account)).extractingJsonPathStringValue("@.name").isEqualTo("Aiden");
		assertThat(json.write(account)).hasJsonPathStringValue("@.type");
		assertThat(json.write(account)).extractingJsonPathStringValue("@.type")
			.isEqualTo(AccountType.SAVINGS.name());
	}

}
