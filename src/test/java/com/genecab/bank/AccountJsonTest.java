package com.genecab.bank;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(SpringExtension.class)
@JsonTest
public class AccountJsonTest {

	@Autowired
	private JacksonTester<Account> json;

	@Autowired
	private JacksonTester<Account[]> jsonList;

	private Account[] accounts;

	@BeforeEach
	void setUp() {
		accounts = Arrays.array(
				new Account(1001L, "Jeremy", "SAVINGS", "sarah1"),
				new Account(1002L, "Emma", "SAVINGS", "sarah1"),
				new Account(1003L, "Sally", "SAVINGS", "sarah1"),
				new Account(1004L, "John", "SAVINGS", "sarah1"),
				new Account(1005L, "Jeremy", "MONEY_MARKET", "sarah1"),
				new Account(1006L, "Jeremy", "CHECKING", "sarah1"));
	}

	@Test
	public void accountSerializationTest() throws IOException {
		Account account = new Account(1001L, "Jeremy", "SAVINGS", "sarah1");
		assertThat(json.write(account)).isStrictlyEqualToJson("single-account.json");
		assertThat(json.write(account)).hasJsonPathNumberValue("@.id");
		assertThat(json.write(account)).extractingJsonPathNumberValue("@.id").isEqualTo(1001);
		assertThat(json.write(account)).hasJsonPathStringValue("@.name");
		assertThat(json.write(account)).extractingJsonPathStringValue("@.name").isEqualTo("Jeremy");
		assertThat(json.write(account)).hasJsonPathStringValue("@.type");
		assertThat(json.write(account)).extractingJsonPathStringValue("@.type")
			.isEqualTo("SAVINGS");
	}

	@Test
	void accountListSerializationTest() throws IOException {
		assertThat(jsonList.write(accounts)).isStrictlyEqualToJson("list-account.json");
	}
	@Test
	void accountListDeserializationTest() throws IOException {
		String expected="""
         [
            { "id": 1001, "name": "Jeremy", "type": "SAVINGS", "owner": "sarah1" },
            { "id": 1002, "name": "Emma", "type": "SAVINGS", "owner": "sarah1" },
            { "id": 1003, "name": "Sally", "type": "SAVINGS", "owner": "sarah1" },
            { "id": 1004, "name": "John", "type": "SAVINGS", "owner": "sarah1" },
            { "id": 1005, "name": "Jeremy", "type": "MONEY_MARKET", "owner": "sarah1" },
            { "id": 1006, "name": "Jeremy", "type": "CHECKING", "owner": "sarah1" }
         ]
         """;
		assertThat(jsonList.parse(expected)).isEqualTo(accounts);
	}

}
