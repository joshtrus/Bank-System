package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawCommandValidatorTest {
	public static final String CD_ID = "34567890";
	public static final String ID_CHECKING = "23456789";
	public static final String ID_SAVINGS = "12345678";
	WithdrawCommandValidator withdrawCommandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		withdrawCommandValidator = new WithdrawCommandValidator(bank);
		bank.addAccount(0.6, 0, ID_SAVINGS, "savings");
		bank.addAccount(0.6, 0, ID_CHECKING, "checking");
		bank.addAccount(0.6, 1500, CD_ID, "cd");
	}

	@Test
	public void withdraw_to_checking_account_that_exists_in_bank() {
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 300");
		assertTrue(actual);
	}

	@Test
	public void withdraw_to_savings_account_that_exists_in_bank() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 600");
		assertTrue(actual);
	}

	@Test
	public void withdraw_to_cd_account_that_exists_in_bank_after_12_months() {
		bank.passTime(12);
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890 2000");
		assertTrue(actual);
	}

	@Test
	public void valid_checking_account_withdrawal_exactly_limit() {
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 400");
		assertTrue(actual);
	}

	@Test
	public void valid_savings_account_withdrawal_exactly_limit() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 1000");
		assertTrue(actual);
	}

	@Test
	public void checking_account_withdrawal_over_limit() {
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 500");
		assertFalse(actual);
	}

	@Test
	public void savings_account_withdrawal_over_limit() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 1100");
		assertFalse(actual);
	}

	@Test
	public void savings_account_withdrawal_negative_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 -100");
		assertFalse(actual);
	}

	@Test
	public void checkings_account_withdrawal_negative_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 -100");
		assertFalse(actual);
	}

	@Test
	public void checkings_account_withdrawal_decimal_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 50.65");
		assertTrue(actual);
	}

	@Test
	public void savings_account_withdrawal_decimal_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 50.65");
		assertTrue(actual);
	}

	@Test
	public void savings_account_withdrawal_minimum_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 0");
		assertTrue(actual);
	}

	@Test
	public void checkings_account_withdrawal_minimum_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 0");
		assertTrue(actual);
	}

	@Test
	public void cd_account_partial_withdraw_of_balance() {
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890 300");
		assertFalse(actual);
	}

	@Test
	public void cd_account_withdraw_more_than_balance() {
		bank.passTime(13);
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890 2000");
		assertTrue(actual);
	}

	@Test
	public void cd_account_withdraw_negative_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890 -100");
		assertFalse(actual);
	}

	@Test
	public void cd_account_withdraw_zero_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890 0");
		assertFalse(actual);
	}

	@Test
	public void withdraw_from_non_existent_account() {
		boolean actual = withdrawCommandValidator.validate("withdraw 66666666 500");
		assertFalse(actual);
	}

	@Test
	public void withdraw_from_account_with_alphanumeric_id() {
		boolean actual = withdrawCommandValidator.validate("withdraw 1234as45 500");
		assertFalse(actual);
	}

	@Test
	public void withdraw_non_numeric_account_from_savings() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void withdraw_non_numeric_account_from_checkings() {
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void withdraw_non_numeric_account_from_cd() {
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void withdraw_command_missing_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890");
		assertFalse(actual);
	}

	@Test
	public void withdraw_command_missing_id() {
		boolean actual = withdrawCommandValidator.validate("withdraw 5000");
		assertFalse(actual);
	}

	@Test
	public void withdraw_command_with_extra_argument() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 300 extra");
		assertFalse(actual);
	}

	@Test
	public void withdraw_from_account_with_special_characters_in_id() {
		boolean actual = withdrawCommandValidator.validate("withdraw 1234@678 500");
		assertFalse(actual);
	}

	@Test
	public void withdraw_from_account_with_special_characters_in_amount() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 $500");
		assertFalse(actual);
	}

	@Test
	public void withdraw_command_with_incorrect_formatting() {
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678-1234");
		assertFalse(actual);
	}

	@Test
	public void withdraw_from_saving_account_twice_in_one_month() {
		bank.minusDollars(300, "12345678");
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 300");
		assertFalse(actual);
	}

	@Test
	public void withdraw_from_saving_account_twice_in_different_months() {
		bank.addDollars(600, "12345678");
		bank.minusDollars(300, "12345678");
		bank.passTime(12);
		boolean actual = withdrawCommandValidator.validate("withdraw 12345678 300");
		assertTrue(actual);
	}

	@Test
	public void withdraw_from_checking_account_twice_in_one_month() {
		bank.minusDollars(300, "23456789");
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 300");
		assertTrue(actual);
	}

	@Test
	public void withdraw_from_checking_account_twice_in_different_months() {
		bank.addDollars(600, "23456789");
		bank.minusDollars(300, "23456789");
		bank.passTime(12);
		boolean actual = withdrawCommandValidator.validate("withdraw 23456789 300");
		assertTrue(actual);
	}

	@Test
	public void withdraw_from_cd_account_before_12_months_has_passed() {
		bank.passTime(11);
		boolean actual = withdrawCommandValidator.validate("withdraw 34567890 1500");
		assertFalse(actual);
	}

}
