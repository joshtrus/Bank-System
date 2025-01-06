package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SavingsTest {

	public static final double APR = 3.5;
	public static final String ID = "12345678";
	Savings savings;

	@BeforeEach
	public void setUp() {
		savings = new Savings(APR, ID);
	}

	@Test
	public void savings_account_starting_balance_is_zero_by_default() {
		double actual = savings.getBalance();

		assertEquals(0, actual);
	}
}
