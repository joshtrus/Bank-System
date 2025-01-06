package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCommandProcessorTest {
	CreateCommandProcessor createCommandProcessor;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		createCommandProcessor = new CreateCommandProcessor(bank);
	}

	@Test
	public void create_a_valid_savings_account() {
		createCommandProcessor.process("Create savings 23456789 0.6");
		boolean actual = bank.accountExistsById("23456789");

		assertTrue(actual);
		assertEquals(bank.retrieveAccount("23456789").getAprValue(), 0.6);
		assertEquals(bank.retrieveAccount("23456789").getType(), "savings");
	}

	@Test
	public void create_a_valid_checking_account() {
		createCommandProcessor.process("Create checking 23456789 0.6");
		boolean actual = bank.accountExistsById("23456789");

		assertTrue(actual);
		assertEquals(bank.retrieveAccount("23456789").getAprValue(), 0.6);
		assertEquals(bank.retrieveAccount("23456789").getType(), "checking");
	}

	@Test
	public void create_a_cd_checking_account() {
		createCommandProcessor.process("Create cd 23456789 0.6 1500");
		boolean actual = bank.accountExistsById("23456789");

		assertTrue(actual);
		assertEquals(bank.retrieveAccount("23456789").getAprValue(), 0.6);
		assertEquals(bank.retrieveAccount("23456789").getType(), "cd");
		assertEquals(bank.retrieveAccount("23456789").getBalance(), 1500);
	}

}
