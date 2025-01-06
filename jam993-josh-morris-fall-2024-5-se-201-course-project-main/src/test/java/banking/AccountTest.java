package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
	public static final double APR = 3.5;
	public static final String ID = "12345678";
	Account checking;

	@BeforeEach
	public void setUp() {
		checking = new Checking(APR, ID);
	}

	@Test
	public void checking_account_starting_balance_is_zero_by_default() {
		double actual = checking.getBalance();

		assertEquals(0, actual);
	}

	@Test
	public void add_100_dollars_to_account() {
		checking.addDollars(100);
		double actual = checking.getBalance();

		assertEquals(100, actual);
	}

	@Test
	public void minus_20_dollars_from_account() {
		checking.addDollars(40);
		checking.minusDollars(20);
		double actual = checking.getBalance();

		assertEquals(20, actual);
	}

	@Test
	public void checking_account_has_APR_value() {
		double actual = checking.getAprValue();
		assertEquals(APR, actual);
	}

	@Test
	public void add_100_dollars_to_account_twice() {
		checking.addDollars(100);
		checking.addDollars(100);
		double actual = checking.getBalance();
		assertEquals(200, actual);
	}

	@Test
	public void minus_20_dollars_from_account_twice() {
		checking.addDollars(40);
		checking.minusDollars(20);
		checking.minusDollars(20);
		double actual = checking.getBalance();
		assertEquals(0, actual);
	}

	@Test
	public void minus_more_dollars_than_available_in_account() {
		checking.addDollars(100);
		checking.minusDollars(150);
		double actual = checking.getBalance();
		assertEquals(0, actual);
	}

	@Test
	public void account_has_8_digit_id() {
		String actual = checking.getId();
		assertEquals(ID, actual);
	}
}
