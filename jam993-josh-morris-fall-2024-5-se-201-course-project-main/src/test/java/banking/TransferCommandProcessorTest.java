package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferCommandProcessorTest {
	TransferCommandProcessor transferCommandProcessor;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		transferCommandProcessor = new TransferCommandProcessor(bank);
		bank.addAccount(0.6, 0, "11111111", "savings");
		bank.addAccount(0.6, 0, "33333333", "checking");
		bank.addAccount(0.6, 1500, "88888888", "cd");
		bank.addAccount(0.6, 0, "22222222", "savings");
		bank.addAccount(0.6, 0, "44444444", "checking");
		bank.addAccount(0.6, 1500, "99999999", "cd");
	}

	@Test
	public void transfer_zero_between_two_checking_accounts() {
		bank.addDollars(300, "33333333");
		transferCommandProcessor.process("transfer 33333333 44444444 0");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("44444444").getBalance();

		assertEquals(source, 300);
		assertEquals(destination, 0);
	}

	@Test
	public void transfer_zero_between_two_savings_accounts() {
		bank.addDollars(300, "11111111");
		transferCommandProcessor.process("transfer 11111111 22222222 0");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source, 300);
		assertEquals(destination, 0);
	}

	@Test
	public void transfer_zero_from_checking_to_saving_account() {
		bank.addDollars(300, "33333333");
		transferCommandProcessor.process("transfer 33333333 22222222 0");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source, 300);
		assertEquals(destination, 0);
	}

	@Test
	public void transfer_zero_from_saving_to_checking_account() {
		bank.addDollars(300, "11111111");
		transferCommandProcessor.process("transfer 11111111 33333333 0");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("33333333").getBalance();

		assertEquals(source, 300);
		assertEquals(destination, 0);
	}

	@Test
	public void transfer_valid_amount_between_savings_accounts() {
		bank.addDollars(300, "11111111");
		bank.addDollars(200, "22222222");
		transferCommandProcessor.process("transfer 11111111 22222222 200");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source, 100);
		assertEquals(destination, 400);

	}

	@Test
	public void transfer_valid_amount_between_checking_accounts() {
		bank.addDollars(300, "33333333");
		bank.addDollars(200, "44444444");
		transferCommandProcessor.process("transfer 33333333 44444444 200");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("44444444").getBalance();

		assertEquals(source, 100);
		assertEquals(destination, 400);

	}

	@Test
	public void transfer_valid_amount_from_saving_to_checking_account() {
		bank.addDollars(300, "11111111");
		bank.addDollars(200, "44444444");
		transferCommandProcessor.process("transfer 11111111 44444444 200");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("44444444").getBalance();

		assertEquals(source, 100);
		assertEquals(destination, 400);
	}

	@Test
	public void transfer_valid_amount_from_checking_to_savings_account() {
		bank.addDollars(300, "44444444");
		bank.addDollars(200, "11111111");
		transferCommandProcessor.process("transfer 44444444 11111111 200");

		double source = bank.retrieveAccount("44444444").getBalance();
		double destination = bank.retrieveAccount("11111111").getBalance();

		assertEquals(source, 100);
		assertEquals(destination, 400);
	}

	@Test
	public void transfer_exact_amount_in_source_account_to_destination_account_between_checking_accounts() {
		bank.addDollars(300, "33333333");
		bank.addDollars(200, "44444444");
		transferCommandProcessor.process("transfer 33333333 44444444 300");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("44444444").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 500);
	}

	@Test
	public void transfer_more_than_amount_in_source_account_to_destination_account_between_checking_accounts() {
		bank.addDollars(200, "33333333");
		bank.addDollars(200, "44444444");
		transferCommandProcessor.process("transfer 33333333 44444444 300");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("44444444").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 400);
	}

	@Test
	public void transfer_exact_amount_in_source_account_to_destination_account_between_savings_accounts() {
		bank.addDollars(500, "11111111");
		bank.addDollars(500, "22222222");
		transferCommandProcessor.process("transfer 11111111 22222222 500");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 1000);
	}

	@Test
	public void transfer_more_than_amount_in_source_account_to_destination_account_between_savings_accounts() {
		bank.addDollars(500, "11111111");
		bank.addDollars(500, "22222222");
		transferCommandProcessor.process("transfer 11111111 22222222 700");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 1000);
	}

	@Test
	public void transfer_exact_amount_in_checking_account_to_savings_account() {
		bank.addDollars(300, "33333333");
		bank.addDollars(500, "11111111");
		transferCommandProcessor.process("transfer 33333333 11111111 300");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("11111111").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 800);
	}

	@Test
	public void transfer_more_than_amount_in_checking_account_to_savings_account() {
		bank.addDollars(300, "33333333");
		bank.addDollars(500, "11111111");
		transferCommandProcessor.process("transfer 33333333 11111111 400");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("11111111").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 800);
	}

	@Test
	public void transfer_exact_amount_in_savings_account_to_checking_account() {
		bank.addDollars(300, "11111111");
		bank.addDollars(500, "33333333");
		transferCommandProcessor.process("transfer 11111111 33333333 300");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("33333333").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 800);
	}

	@Test
	public void transfer_more_than_amount_in_savings_account_to_checking_account() {
		bank.addDollars(300, "11111111");
		bank.addDollars(500, "33333333");
		transferCommandProcessor.process("transfer 11111111 33333333 400");

		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("33333333").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 800);
	}

	@Test
	public void transfer_where_source_account_has_a_balance_of_zero() {
		bank.addDollars(300, "22222222");

		transferCommandProcessor.process("transfer 11111111 22222222 300");
		double source = bank.retrieveAccount("11111111").getBalance();
		double destination = bank.retrieveAccount("22222222").getBalance();

		assertEquals(source, 0);
		assertEquals(destination, 300);
	}

	@Test
	public void mutliple_transfers_between_checking_account() {
		bank.addDollars(800, "33333333");

		transferCommandProcessor.process("transfer 33333333 44444444 100");
		transferCommandProcessor.process("transfer 33333333 44444444 300");

		double source = bank.retrieveAccount("33333333").getBalance();
		double destination = bank.retrieveAccount("44444444").getBalance();

		assertEquals(source, 400);
		assertEquals(destination, 400);

	}

}
