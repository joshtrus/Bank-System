package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawCommandProcessorTest {
	WithdrawCommandProcessor withdrawCommandProcessor;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		withdrawCommandProcessor = new WithdrawCommandProcessor(bank);
		bank.addAccount(0.6, 0, "12345678", "savings");
		bank.addAccount(0.6, 0, "23456789", "checking");
		bank.addAccount(0.6, 1500, "34567890", "cd");

	}

	@Test
	public void withdraw_from_savings_subtracts_amount_from_balance() {
		bank.addDollars(500, "12345678");
		withdrawCommandProcessor.process("withdraw 12345678 0");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(500, actual);

	}

	@Test
	public void withdraw_from_checkings_subtracts_amount_from_balance() {
		bank.addDollars(500, "23456789");
		withdrawCommandProcessor.process("withdraw 23456789 0");
		double actual = bank.retrieveAccount("23456789").getBalance();

		assertEquals(500, actual);

	}

	@Test
	public void withdraw_less_than_min_from_savings_subtracts_amount_from_balance() {
		bank.addDollars(800, "12345678");
		withdrawCommandProcessor.process("withdraw 12345678 400");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(400, actual);

	}

	@Test
	public void withdraw_max_amount_from_savings_subtracts_amount_from_balance() {
		bank.addDollars(1000, "12345678");
		withdrawCommandProcessor.process("withdraw 12345678 1000");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(0, actual);
	}

	@Test
	public void withdraw_amount_more_than_balance_in_savings_account_zeros_balance() {
		bank.addDollars(800, "12345678");
		withdrawCommandProcessor.process("withdraw 12345678 1000");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(0, actual);
	}

	@Test
	public void withdraw_less_than_min_from_checkings_subtracts_amount_from_balance() {
		bank.addDollars(800, "23456789");
		withdrawCommandProcessor.process("withdraw 23456789 300");
		double actual = bank.retrieveAccount("23456789").getBalance();

		assertEquals(500, actual);

	}

	@Test
	public void withdraw_max_amount_from_checkings_subtracts_amount_from_balance() {
		bank.addDollars(1000, "23456789");
		withdrawCommandProcessor.process("withdraw 23456789 400");
		double actual = bank.retrieveAccount("23456789").getBalance();

		assertEquals(600, actual);
	}

	@Test
	public void withdraw_amount_more_than_balance_in_checking_account_zeros_balance() {
		bank.addDollars(200, "23456789");
		withdrawCommandProcessor.process("withdraw 12345678 300");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(0, actual);
	}

	@Test
	public void withdraw_from_checking_twice() {
		bank.addDollars(500, "23456789");
		withdrawCommandProcessor.process("withdraw 23456789 100");
		withdrawCommandProcessor.process("withdraw 23456789 200");
		double actual = bank.retrieveAccount("23456789").getBalance();

		assertEquals(200, actual);
	}

	@Test
	public void withdraw_from_savings_twice_after_month_has_passed() {
		bank.addDollars(500, "12345678");
		withdrawCommandProcessor.process("withdraw 12345678 100");
		bank.passTime(1);
		withdrawCommandProcessor.process("withdraw 12345678 200");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(200.2, actual);
	}

	@Test
	public void withdraw_from_cd_account_after_twelve_months_has_passed() {
		bank.passTime(12);
		withdrawCommandProcessor.process("withdraw 34567890 1500");
		double actual = bank.retrieveAccount("34567890").getBalance();

		assertEquals(36.398651918106225, actual);

	}

	@Test
	public void withdraw_more_than_balance_from_cd_account_after_twelve_months_has_passed() {
		bank.passTime(12);
		withdrawCommandProcessor.process("withdraw 34567890 3000");
		double actual = bank.retrieveAccount("34567890").getBalance();

		assertEquals(0, actual);
	}

}
