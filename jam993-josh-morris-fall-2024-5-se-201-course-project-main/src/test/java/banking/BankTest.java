package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {

	public static final String ID_1 = "12345678";
	public static final String ID_2 = "23456789";
	public static final String SAVINGS = "savings";
	public static final String CHECKING = "checking";
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
	}

	@Test
	public void bank_has_no_accounts_when_created() {
		int actual = bank.getAccounts().size();

		assertEquals(0, actual);
	}

	@Test
	public void adding_one_account_to_the_bank() {
		bank.addAccount(3.5, 0, ID_1, SAVINGS);
		int actual = bank.getAccounts().size();

		assertEquals(1, actual);
	}

	@Test
	public void adding_two_accounts_to_the_bank() {
		bank.addAccount(3.5, 0, ID_1, SAVINGS);
		bank.addAccount(2.3, 0, ID_2, CHECKING);
		int actual = bank.getAccounts().size();

		assertEquals(2, actual);
	}

	@Test
	public void retrieve_account_using_id() {
		bank.addAccount(3.5, 0, ID_1, SAVINGS);
		Account actual = bank.retrieveAccount(ID_1);

		assertEquals(ID_1, actual.getId());
	}

	@Test
	public void deposit_money_by_ID_to_savings_account() {
		bank.addAccount(3.5, 0, ID_1, SAVINGS);
		bank.addDollars(30, ID_1);
		double actual = bank.retrieveAccount(ID_1).getBalance();

		assertEquals(30, actual);
	}

	@Test
	public void withdraw_money_by_ID_from_savings_account() {
		bank.addAccount(3.5, 0, ID_1, SAVINGS);
		bank.addDollars(30, ID_1);
		bank.minusDollars(10, ID_1);
		double actual = bank.retrieveAccount(ID_1).getBalance();

		assertEquals(20, actual);
	}

	@Test
	public void deposit_money_twice_by_ID_to_savings_account() {
		bank.addAccount(3.5, 0, ID_1, SAVINGS);
		bank.addDollars(30, ID_1);
		bank.addDollars(10, ID_1);
		double actual = bank.retrieveAccount(ID_1).getBalance();

		assertEquals(40, actual);
	}

	@Test
	public void withdraw_money_twice_by_ID_from_savings_account() {
		bank.addAccount(3.5, 0, ID_1, SAVINGS);
		bank.addDollars(100, ID_1);
		bank.minusDollars(10, ID_1);
		bank.minusDollars(20, ID_1);
		double actual = bank.retrieveAccount(ID_1).getBalance();

		assertEquals(70, actual);

	}

}
