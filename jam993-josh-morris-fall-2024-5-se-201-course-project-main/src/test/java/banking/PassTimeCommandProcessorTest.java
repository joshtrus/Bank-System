package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeCommandProcessorTest {
	PassTimeCommandProcessor passTimeCommandProcessor;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		passTimeCommandProcessor = new PassTimeCommandProcessor(bank);
	}

	@Test
	public void savings_account_with_balance_less_than_100_decreases_by_25() {
		bank.addAccount(0.6, 0, "11111111", "savings");
		bank.addDollars(50, "11111111");
		passTimeCommandProcessor.process("pass 1");

		double actual = bank.retrieveAccount("11111111").getBalance();

		assertEquals(actual, 25.0125);
	}

	@Test
	public void checking_account_with_balance_less_than_100_decreases_by_25() {
		bank.addAccount(0.6, 0, "11111111", "checking");
		bank.addDollars(50, "11111111");
		passTimeCommandProcessor.process("pass 1");

		double actual = bank.retrieveAccount("11111111").getBalance();

		assertEquals(actual, 25.0125);
	}

	@Test
	public void several_months_passing_accrues_savings_apr_by_valid_amount() {
		bank.addAccount(0.6, 0, "11111111", "savings");
		bank.addDollars(500, "11111111");
		passTimeCommandProcessor.process("pass 12");

		double actual = bank.retrieveAccount("11111111").getBalance();
		assertEquals(actual, 503.00826376548116);
	}

	@Test
	public void several_months_passing_accrues_checking_apr_by_valid_amount() {
		bank.addAccount(0.6, 0, "11111111", "checking");
		bank.addDollars(500, "11111111");
		passTimeCommandProcessor.process("pass 12");

		double actual = bank.retrieveAccount("11111111").getBalance();
		assertEquals(actual, 503.00826376548116);
	}

	@Test
	public void several_months_passing_accrues_cd_apr_by_valid_amount() {
		bank.addAccount(0.6, 1500, "11111111", "cd");
		passTimeCommandProcessor.process("pass 12");

		double actual = bank.retrieveAccount("11111111").getBalance();
		assertEquals(actual, 1536.3986519181062);
	}

	@Test
	public void max_months_passing_accrues_savings_apr_by_valid_amount() {
		bank.addAccount(0.6, 0, "11111111", "savings");
		bank.addDollars(500, "11111111");
		passTimeCommandProcessor.process("pass 60");

		double actual = bank.retrieveAccount("11111111").getBalance();
		assertEquals(actual, 515.2234040743228);
	}

	@Test
	public void max_months_passing_accrues_checking_apr_by_valid_amount() {
		bank.addAccount(0.6, 0, "11111111", "checking");
		bank.addDollars(500, "11111111");
		passTimeCommandProcessor.process("pass 60");

		double actual = bank.retrieveAccount("11111111").getBalance();
		assertEquals(actual, 515.2234040743228);
	}

	@Test
	public void max_months_passing_accrues_cd_apr_by_valid_amount() {
		bank.addAccount(0.6, 1500, "11111111", "cd");
		passTimeCommandProcessor.process("pass 60");

		double actual = bank.retrieveAccount("11111111").getBalance();
		assertEquals(actual, 1691.0426102738547);
	}
}
