package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositCommandProcessorTest {
	DepositCommandProcessor depositCommandProcessor;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		depositCommandProcessor = new DepositCommandProcessor(bank);
	}

	@Test
	public void deposit_minimum_amount_to_savings_account() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		depositCommandProcessor.process("deposit 12345678 0");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void deposit_minimum_amount_to_checking_account() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		depositCommandProcessor.process("deposit 12345678 0");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void deposit_maximum_amount_to_saving_account() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		depositCommandProcessor.process("deposit 12345678 2500");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 2500);
	}

	@Test
	public void deposit_maximum_amount_to_checking_account() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		depositCommandProcessor.process("deposit 12345678 1000");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 1000);
	}

	@Test
	public void depositing_to_empty_savings_account_increases_balance_by_added_amount() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		depositCommandProcessor.process("deposit 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 300);
	}

	@Test
	public void depositing_to_empty_checking_account_increases_balance_by_added_amount() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		depositCommandProcessor.process("deposit 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 300);
	}

	@Test
	public void depositing_to_savings_account_with_existing_balance_increases_balance_by_added_amount() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		bank.addDollars(400, "12345678");

		depositCommandProcessor.process("deposit 12345678 400");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 800);
	}

	@Test
	public void depositing_to_checking_account_with_existing_balance_increases_balance_by_added_amount() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		bank.addDollars(400, "12345678");

		depositCommandProcessor.process("deposit 12345678 400");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 800);
	}

	@Test
	public void depositing_decimal_value_to_savings_increases_balance_by_added_amount() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		depositCommandProcessor.process("deposit 12345678 30.50");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 30.50);
	}

	@Test
	public void depositing_decimal_value_to_checking_increases_balance_by_added_amount() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		depositCommandProcessor.process("deposit 12345678 30.50");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 30.50);
	}

}
