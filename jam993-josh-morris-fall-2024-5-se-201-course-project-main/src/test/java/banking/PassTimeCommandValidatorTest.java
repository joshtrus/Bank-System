package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeCommandValidatorTest {
	PassTimeCommandValidator passTimeCommandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		passTimeCommandValidator = new PassTimeCommandValidator(bank);
	}

	@Test
	public void valid_month_passed() {
		boolean actual = passTimeCommandValidator.validate("pass 1");
		assertTrue(actual);
	}

	@Test
	public void valid_double_digit_month_passed() {
		boolean actual = passTimeCommandValidator.validate("pass 12");
		assertTrue(actual);
	}

	@Test
	public void max_amount_of_months_passed() {
		boolean actual = passTimeCommandValidator.validate("pass 60");
		assertTrue(actual);
	}

	@Test
	public void extra_arguments_in_pass_command() {
		boolean actual = passTimeCommandValidator.validate("pass 12 now");
		assertFalse(actual);
	}

	@Test
	public void non_integer_months_used_in_pass_command() {
		boolean actual = passTimeCommandValidator.validate("pass twelve");
		assertFalse(actual);
	}

	@Test
	public void decimal_months_used_in_pass_command() {
		boolean actual = passTimeCommandValidator.validate("pass 1.5");
		assertFalse(actual);
	}

	@Test
	public void negative_number_used_as_month_in_pass_command() {
		boolean actual = passTimeCommandValidator.validate("pass -1");
		assertFalse(actual);
	}

	@Test
	public void number_below_minimum_amount_of_months_used_in_command() {
		boolean actual = passTimeCommandValidator.validate("pass 0");
		assertFalse(actual);
	}

	@Test
	public void number_above_max_amount_of_months_used_in_command() {
		boolean actual = passTimeCommandValidator.validate("pass 61");
		assertFalse(actual);
	}

	@Test
	public void alphanumeric_month_value_used_in_pass_command() {
		boolean actual = passTimeCommandValidator.validate("pass 1abc");
		assertFalse(actual);
	}

	@Test
	public void month_value_with_special_characters_used_in_pass_command() {
		boolean actual = passTimeCommandValidator.validate("pass 13#");
		assertFalse(actual);
	}

	@Test
	public void missing_amount_of_months() {
		boolean actual = passTimeCommandValidator.validate("pass");
		assertFalse(actual);
	}

}