package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckingTest {

	public static final double APR = 3.5;
	public static final String ID = "12345678";
	Checking checking;

	@BeforeEach
	public void setUp() {
		checking = new Checking(APR, ID);
	}

	@Test
	public void checking_account_starting_balance_is_zero_by_default() {
		double actual = checking.getBalance();

		assertEquals(0, actual);
	}

}
