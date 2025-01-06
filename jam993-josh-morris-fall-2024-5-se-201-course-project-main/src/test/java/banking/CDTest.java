package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CDTest {

	public static final double APR = 3.5;
	public static final double DOLLARS = 500;
	public static final String ID = "12345678";
	CD cd;

	@BeforeEach
	public void setUp() {
		cd = new CD(APR, DOLLARS, ID);
	}

	@Test
	public void account_is_created_with_specific_balance() {
		double actual = cd.getBalance();

		assertEquals(500, actual);

	}
}
