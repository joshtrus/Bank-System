package banking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandProcessorTest {
	private CommandProcessor commandProcessor;
	private Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);

	}

	@Test
	public void processing_a_valid_create_savings_account_command() {
		commandProcessor.process("create savings 12345678 0.6");
		boolean actual = bank.accountExistsById("12345678");

		assertTrue(actual);
		assertEquals(bank.retrieveAccount("12345678").getType(), "savings");
	}

	@Test
	public void processing_a_valid_create_checking_account_command() {
		commandProcessor.process("create checking 12345678 0.6");
		boolean actual = bank.accountExistsById("12345678");

		assertTrue(actual);
		assertEquals(bank.retrieveAccount("12345678").getType(), "checking");
	}

	@Test
	public void processing_a_valid_create_cd_account_command() {
		commandProcessor.process("create cd 12345678 0.6");
		boolean actual = bank.accountExistsById("12345678");

		assertTrue(actual);
		assertEquals(bank.retrieveAccount("12345678").getType(), "cd");
	}

	@Test
	public void process_depositing_valid_amount_to_an_empty_checking_account() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		commandProcessor.process("deposit 12345678 300");

		assertEquals(bank.retrieveAccount("12345678").getBalance(), 300);
	}

	@Test
	public void process_depositing_valid_amount_to_an_empty_savings_account() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		commandProcessor.process("deposit 12345678 300");

		assertEquals(bank.retrieveAccount("12345678").getBalance(), 300);
	}

	@Test
	public void process_depositing_valid_amount_to_a_checking_account_with_existing_balance() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		bank.addDollars(400, "12345678");

		commandProcessor.process("deposit 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 700);
	}

	@Test
	public void process_depositing_valid_amount_to_a_savings_account_with_existing_balance() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		bank.addDollars(400, "12345678");

		commandProcessor.process("deposit 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 700);
	}

	@Test
	public void process_withdrawing_valid_amount_from_a_checking_account_with_existing_balance() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		bank.addDollars(400, "12345678");

		commandProcessor.process("withdraw 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 100);
	}

	@Test
	public void process_withdrawing_valid_amount_twice_from_a_checking_account_with_existing_balance() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		bank.addDollars(800, "12345678");

		commandProcessor.process("withdraw 12345678 300");
		commandProcessor.process("withdraw 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 200);
	}

	@Test
	public void process_withdrawing_valid_amount_from_a_savings_account_with_existing_balance() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		bank.addDollars(400, "12345678");

		commandProcessor.process("withdraw 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 100);
	}

	@Test
	public void process_withdrawing_valid_amount_twice_from_a_savings_account_with_existing_balance() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		bank.addDollars(800, "12345678");

		commandProcessor.process("withdraw 12345678 300");
		commandProcessor.process("withdraw 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 200);
	}

	@Test
	public void process_withdrawing_amount_greater_than_balance_from_checking_account() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		bank.addDollars(100, "12345678");

		commandProcessor.process("withdraw 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void process_withdrawing_amount_greater_than_balance_from_savings_account() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		bank.addDollars(500, "12345678");

		commandProcessor.process("withdraw 12345678 1000");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void process_withdrawing_amount_from_empty_checking_account() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		bank.addDollars(0, "12345678");

		commandProcessor.process("withdraw 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void process_withdrawing_amount_from_empty_savings_account() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		bank.addDollars(0, "12345678");

		commandProcessor.process("withdraw 12345678 300");
		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void process_withdrawing_amount_from_cd_account() {
		bank.addAccount(0.1, 1500, "12345678", "cd");
		commandProcessor.process("withdraw 12345678 1500");

		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void process_withdrawing_amount_more_than_balance_from_cd_account() {
		bank.addAccount(0.1, 1500, "12345678", "cd");
		commandProcessor.process("withdraw 12345678 2000");

		assertEquals(bank.retrieveAccount("12345678").getBalance(), 0);
	}

	@Test
	public void transfer_between_savings_accounts() {
		bank.addAccount(0.1, 0, "11111111", "savings");
		bank.addAccount(0.1, 0, "22222222", "savings");
		bank.addDollars(500, "11111111");

		commandProcessor.process("transfer 11111111 22222222 300");

		double source_balance = bank.retrieveAccount("11111111").getBalance();
		double destination_balance = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source_balance, 200);
		assertEquals(destination_balance, 300);
	}

	@Test
	public void transfer_between_checking_accounts() {
		bank.addAccount(0.1, 0, "11111111", "checking");
		bank.addAccount(0.1, 0, "22222222", "checking");
		bank.addDollars(500, "11111111");

		commandProcessor.process("transfer 11111111 22222222 300");

		double source_balance = bank.retrieveAccount("11111111").getBalance();
		double destination_balance = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source_balance, 200);
		assertEquals(destination_balance, 300);
	}

	@Test
	public void transfer_from_checking_to_saving_accounts() {
		bank.addAccount(0.1, 0, "11111111", "checking");
		bank.addAccount(0.1, 0, "22222222", "savings");
		bank.addDollars(500, "11111111");

		commandProcessor.process("transfer 11111111 22222222 300");

		double source_balance = bank.retrieveAccount("11111111").getBalance();
		double destination_balance = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source_balance, 200);
		assertEquals(destination_balance, 300);
	}

	@Test
	public void transfer_from_saving_to_checkings_accounts() {
		bank.addAccount(0.1, 0, "11111111", "savings");
		bank.addAccount(0.1, 0, "22222222", "checking");
		bank.addDollars(500, "11111111");

		commandProcessor.process("transfer 11111111 22222222 300");

		double source_balance = bank.retrieveAccount("11111111").getBalance();
		double destination_balance = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source_balance, 200);
		assertEquals(destination_balance, 300);
	}

	@Test
	public void pass_time_increases_time() {
		bank.addAccount(0.1, 0, "11111111", "savings");
		bank.addDollars(500, "11111111");
		commandProcessor.process("pass 12");

		assertEquals(12, bank.retrieveAccount("11111111").getPassTime());
	}

	@Test
	public void pass_time_removes_empty_savings_acccount() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		commandProcessor.process("pass 12");
		Account actual = bank.retrieveAccount("12345678");
		assertNull(actual);
	}

	@Test
	public void pass_time_removes_empty_checking_acccount() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		commandProcessor.process("pass 12");
		Account actual = bank.retrieveAccount("12345678");
		assertNull(actual);
	}

	@Test
	public void pass_time_removes_empty_cd_acccount() {
		bank.addAccount(0.1, 1500, "12345678", "cd");
		commandProcessor.process("pass 12");
		double balance = bank.retrieveAccount("12345678").getBalance();
		bank.minusDollars(balance, "12345678");
		commandProcessor.process("pass 1");

		Account actual = bank.retrieveAccount("12345678");
		assertNull(actual);
	}

	@Test
	void pass_time_accrues_savings_apr() {
		bank.addAccount(0.1, 0, "12345678", "savings");
		bank.addDollars(600, "12345678");
		commandProcessor.process("pass 1");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(600.05, actual);
	}

	@Test
	void pass_time_accrues_checkings_apr() {
		bank.addAccount(0.1, 0, "12345678", "checking");
		bank.addDollars(600, "12345678");
		commandProcessor.process("pass 1");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(600.05, actual);
	}

	@Test
	void pass_time_accrues_cd_apr() {
		bank.addAccount(2.1, 1500, "12345678", "cd");
		commandProcessor.process("pass 1");
		double actual = bank.retrieveAccount("12345678").getBalance();

		assertEquals(1510.5, actual);
	}

}
