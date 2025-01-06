package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionHistoryTest {
	Bank bank;
	TransactionHistory transactionHistory;
	List<String> validCommands;
	List<String> invalidCommands;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		transactionHistory = new TransactionHistory(bank);
		validCommands = new ArrayList<>();
		invalidCommands = new ArrayList<>();
	}

	@Test
	public void savings_account_type_capitalized() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[0];
		assertEquals("Savings", actual);
	}

	@Test
	public void checking_account_type_capitalized() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[0];
		assertEquals("Checking", actual);
	}

	@Test
	public void cd_account_type_capitalized() {
		bank.addAccount(0.6, 1500, "12345678", "cd");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[0];
		assertEquals("Cd", actual);
	}

	@Test
	public void correct_id_present_in_account_status() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[1];

		assertEquals("12345678", actual);
	}

	@Test
	public void correct_account_typ_present_in_account_status() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[1];

		assertEquals("12345678", actual);
	}

	@Test
	public void balance_for_savings_account_truncated_to_two_decimals() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		bank.addDollars(500.66734, "12345678");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[2];

		assertEquals("500.66", actual);
	}

	@Test
	public void balance_for_checking_account_truncated_to_two_decimals() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		bank.addDollars(500.66734, "12345678");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[2];

		assertEquals("500.66", actual);
	}

	@Test
	public void balance_for_cd_account_truncated_to_two_decimals() {
		bank.addAccount(0.6, 1500.7867564, "12345678", "cd");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[2];

		assertEquals("1500.78", actual);
	}

	@Test
	public void apr_for_account_truncated_to_two_decimals() {
		bank.addAccount(0.6455643, 0, "12345678", "savings");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[3];

		assertEquals("0.64", actual);
	}

	@Test
	public void whole_number_apr_for_account_truncated_to_two_decimals() {
		bank.addAccount(3, 0, "12345678", "savings");
		String status = transactionHistory.getAccountState("12345678");
		String[] parts = status.split(" ");
		String actual = parts[3];

		assertEquals("3.00", actual);
	}

	@Test
	public void account_status_for_empty_savings_account_is_valid() {
		bank.addAccount(0.0, 0, "12345678", "savings");
		String status = transactionHistory.getAccountState("12345678");

		assertEquals("Savings 12345678 0.00 0.00", status);
	}

	@Test
	public void account_status_for_empty_checking_account_is_valid() {
		bank.addAccount(0.0, 0, "12345678", "checking");
		String status = transactionHistory.getAccountState("12345678");

		assertEquals("Checking 12345678 0.00 0.00", status);
	}

	@Test
	public void account_status_for_cd_account_with_money_is_valid() {
		bank.addAccount(0.6, 1500, "12345678", "cd");
		String status = transactionHistory.getAccountState("12345678");

		assertEquals("Cd 12345678 1500.00 0.60", status);
	}

	@Test
	public void account_status_for_checking_account_with_money_is_valid() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		bank.addDollars(500, "12345678");
		String status = transactionHistory.getAccountState("12345678");

		assertEquals("Checking 12345678 500.00 0.60", status);
	}

	@Test
	public void account_status_for_savings_account_with_money_is_valid() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		bank.addDollars(500, "12345678");
		String status = transactionHistory.getAccountState("12345678");

		assertEquals("Savings 12345678 500.00 0.60", status);
	}

	@Test
	public void get_transaction_history_for_savings_account_using_all_commands_both_valid_and_invalid() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		bank.addAccount(0.6, 0, "23456789", "checking");
		validCommands.add("Create savings 12345678 0.6");
		bank.addDollars(1000, "12345678");
		validCommands.add("Deposit 12345678 1000");
		bank.minusDollars(300, "12345678");
		invalidCommands.add("Withdraw 12345678 5000");
		validCommands.add("Withdraw 12345678 300");
		bank.transferDollars("12345678", "23456789", 400);
		validCommands.add("Transfer 12345678 23456789 400");
		invalidCommands.add("Transfer 12345678 12345678 300");

		List<String> actual = transactionHistory.getTransactionHistory(validCommands, invalidCommands);

		assertEquals("Savings 12345678 300.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 1000", actual.get(1));
		assertEquals("Withdraw 12345678 300", actual.get(2));
		assertEquals("Transfer 12345678 23456789 400", actual.get(3));
		assertEquals("Checking 23456789 400.00 0.60", actual.get(4));
		assertEquals("Transfer 12345678 23456789 400", actual.get(5));
		assertEquals("Withdraw 12345678 5000", actual.get(6));
		assertEquals("Transfer 12345678 12345678 300", actual.get(7));
	}

	@Test
	public void get_transaction_history_for_checking_account_using_all_commands_both_valid_and_invalid() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		bank.addAccount(0.6, 0, "23456789", "savings");
		validCommands.add("Create checking 12345678 0.6");
		bank.addDollars(800, "12345678");
		validCommands.add("Deposit 12345678 800");
		bank.minusDollars(300, "12345678");
		invalidCommands.add("Withdraw 12345678 5000");
		validCommands.add("Withdraw 12345678 300");
		bank.transferDollars("12345678", "23456789", 300);
		validCommands.add("Transfer 12345678 23456789 300");
		invalidCommands.add("Transfer 12345678 12345678 300");

		List<String> actual = transactionHistory.getTransactionHistory(validCommands, invalidCommands);

		assertEquals("Checking 12345678 200.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 800", actual.get(1));
		assertEquals("Withdraw 12345678 300", actual.get(2));
		assertEquals("Transfer 12345678 23456789 300", actual.get(3));
		assertEquals("Savings 23456789 300.00 0.60", actual.get(4));
		assertEquals("Transfer 12345678 23456789 300", actual.get(5));
		assertEquals("Withdraw 12345678 5000", actual.get(6));
		assertEquals("Transfer 12345678 12345678 300", actual.get(7));
	}

	@Test
	public void get_transaction_history_for_cd_account_using_all_commands_both_valid_and_invalid() {
		bank.addAccount(0.6, 1500, "12345678", "cd");
		bank.passTime(12);
		validCommands.add("Create cd 12345678 0.6 1500");
		validCommands.add("pass 12");

		double balance = bank.retrieveAccount("12345678").getBalance();
		bank.minusDollars(balance, "12345678");
		String formatBalance = Double.toString(balance);

		validCommands.add("Withdraw 12345678 " + formatBalance);
		invalidCommands.add("Deposit 12345678 500");
		invalidCommands.add("Transfer 12345678 23456789 300");

		List<String> actual = transactionHistory.getTransactionHistory(validCommands, invalidCommands);

		assertEquals("Cd 12345678 0.00 0.60", actual.get(0));
		assertEquals("Withdraw 12345678 " + formatBalance, actual.get(1));
		assertEquals("Deposit 12345678 500", actual.get(2));
		assertEquals("Transfer 12345678 23456789 300", actual.get(3));
	}

	@Test
	public void get_transaction_history_of_savings_account_after_only_invalid_commands() {
		bank.addAccount(0.6, 0, "12345678", "savings");
		invalidCommands.add("Withdraw 12345678 5000");
		invalidCommands.add("Deposit 12345678 6000");

		List<String> actual = transactionHistory.getTransactionHistory(validCommands, invalidCommands);
		assertEquals("Savings 12345678 0.00 0.60", actual.get(0));
		assertEquals("Withdraw 12345678 5000", actual.get(1));
		assertEquals("Deposit 12345678 6000", actual.get(2));
	}

	@Test
	public void get_transaction_history_of_checking_account_after_only_invalid_commands() {
		bank.addAccount(0.6, 0, "12345678", "checking");
		invalidCommands.add("Withdraw 12345678 5000");
		invalidCommands.add("Deposit 12345678 6000");

		List<String> actual = transactionHistory.getTransactionHistory(validCommands, invalidCommands);
		assertEquals("Checking 12345678 0.00 0.60", actual.get(0));
		assertEquals("Withdraw 12345678 5000", actual.get(1));
		assertEquals("Deposit 12345678 6000", actual.get(2));
	}

	@Test
	public void get_transaction_history_of_cd_account_after_only_invalid_commands() {
		bank.addAccount(0.6, 1500, "12345678", "cd");
		invalidCommands.add("Withdraw 12345678 5000");
		invalidCommands.add("Deposit 12345678 6000");

		List<String> actual = transactionHistory.getTransactionHistory(validCommands, invalidCommands);
		assertEquals("Cd 12345678 1500.00 0.60", actual.get(0));
		assertEquals("Withdraw 12345678 5000", actual.get(1));
		assertEquals("Deposit 12345678 6000", actual.get(2));
	}

}
