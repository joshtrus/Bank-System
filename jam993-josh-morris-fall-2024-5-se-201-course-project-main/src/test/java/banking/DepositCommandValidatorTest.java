package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositCommandValidatorTest {
	public static final String CD_ID = "34567890";
	public static final String ID_CHECKINGS = "23456789";
	public static final String ID_SAVINGS = "12345678";
	DepositCommandValidator depositCommandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		depositCommandValidator = new DepositCommandValidator(bank);
		bank.addAccount(0.6, 0, ID_SAVINGS, "savings");
		bank.addAccount(0.6, 0, ID_CHECKINGS, "checking");
		bank.addAccount(0.6, 1500, CD_ID, "cd");
	}

	@Test
	public void deposit_to_checking_account_that_exists_in_bank() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void deposit_to_savings_account_that_exists_in_bank() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 1000");
		assertTrue(actual);
	}

	@Test
	public void deposit_to_cd_account_that_exists_in_bank() {
		boolean actual = depositCommandValidator.validate("deposit 34567890 1500");
		assertFalse(actual);
	}

	@Test
	public void deposit_to_cd_acount_with_max_savings_deposit_amount() {
		boolean actual = depositCommandValidator.validate("deposit 34567890 2500");
		assertFalse(actual);
	}

	@Test
	public void deposit_to_cd_acount_with_max_checking_deposit_amount() {
		boolean actual = depositCommandValidator.validate("deposit 34567890 1000");
		assertFalse(actual);
	}

	@Test
	public void deposit_to_cd_acount_with_min_savings_deposit_amount() {
		boolean actual = depositCommandValidator.validate("deposit 34567890 0");
		assertFalse(actual);
	}

	@Test
	public void deposit_to_cd_acount_with_min_checking_deposit_amount() {
		boolean actual = depositCommandValidator.validate("deposit 34567890 0");
		assertFalse(actual);
	}

	@Test
	public void valid_checking_account_deposit_with_amount_within_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void valid_savings_account_deposit_with_amount_within_limit() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 1000");
		assertTrue(actual);
	}

	@Test
	public void valid_checking_account_deposit_without_amount_over_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 5000");
		assertFalse(actual);
	}

	@Test
	public void valid_savings_account_deposit_without_amount_over_limit() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 5000");
		assertFalse(actual);
	}

	@Test
	public void valid_checking_account_deposit_without_amount_below_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 -5000");
		assertFalse(actual);
	}

	@Test
	public void valid_savings_account_deposit_without_amount_below_limit() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 -5000");
		assertFalse(actual);
	}

	@Test
	public void valid_savings_account_deposit_without_amount_at_upper_limit() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 2500");
		assertTrue(actual);
	}

	@Test
	public void valid_savings_account_deposit_without_amount_at_lower_limit() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 0");
		assertTrue(actual);
	}

	@Test
	public void valid_checking_account_deposit_without_amount_at_upper_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 1000");
		assertTrue(actual);
	}

	@Test
	public void valid_checking_account_deposit_without_amount_at_lower_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 0");
		assertTrue(actual);
	}

	@Test
	public void deposit_into_non_existent_account() {
		boolean actual = depositCommandValidator.validate("deposit 345632267 1000");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_an_account_using_alphabetic_id() {
		boolean actual = depositCommandValidator.validate("deposit ABCDEFGH 1000");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_an_account_using_alphanumeric_id() {
		boolean actual = depositCommandValidator.validate("deposit 123ABCDEF 1000");
		assertFalse(actual);
	}

	@Test
	public void saving_account_deposit_over_limit_by_a_decimal_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 2500.50");
		assertFalse(actual);
	}

	@Test
	public void saving_account_deposit_with_smallest_positive_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 0.01");
		assertTrue(actual);
	}

	@Test
	public void savings_account_deposit_with_decimal_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 1000.50");
		assertTrue(actual);
	}

	@Test
	public void savings_account_deposit_with_decimal_amount_just_below_limit() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 2499.99");
		assertTrue(actual);
	}

	@Test
	public void checking_account_deposit_with_decimal_amount_over_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 1000.50");
		assertFalse(actual);
	}

	@Test
	public void checking_account_deposit_with_decimal_amount_within_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 500.50");
		assertTrue(actual);
	}

	@Test
	public void checking_account_deposit_with_smallest_decimal_amount_just_above_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 0.01");
		assertTrue(actual);
	}

	@Test
	public void checking_account_deposit_with_largest_decimal_amount_just_below_limit() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 999.99");
		assertTrue(actual);
	}

	@Test
	public void checking_account_deposit_with_non_numeric_deposit_amount() {
		boolean actual = depositCommandValidator.validate("deposit 23456788 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void saving_account_deposit_with_non_numeric_deposit_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void savings_account_deposit_with_missing_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678");
		assertFalse(actual);
	}

	@Test
	public void savings_account_deposit_with_missing_id() {
		boolean actual = depositCommandValidator.validate("deposit 500");
		assertFalse(actual);
	}

	@Test
	public void checking_account_deposit_with_missing_amount() {
		boolean actual = depositCommandValidator.validate("deposit 23456789");
		assertFalse(actual);
	}

	@Test
	public void checking_account_deposit_with_missing_id() {
		boolean actual = depositCommandValidator.validate("deposit 500");
		assertFalse(actual);
	}

	@Test
	public void checking_account_deposit_with_extra_argument() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 1000 extra");
		assertFalse(actual);
	}

	@Test
	public void savings_account_deposit_with_extra_argument() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 1000 extra");
		assertFalse(actual);
	}

	@Test
	public void checking_account_deposit_with_minimum_integer_amount() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 1");
		assertTrue(actual);
	}

	@Test
	public void saving_account_deposit_with_minimum_integer_amount() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 1");
		assertTrue(actual);
	}

	@Test
	public void depost_command_with_no_id_or_amount_argument() {
		boolean actual = depositCommandValidator.validate("deposit");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_account_with_special_characters_in_id() {
		boolean actual = depositCommandValidator.validate("deposit 23@56789 500");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_savings_account_with_special_characters_in_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 $500");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_checking_account_with_special_characters_in_amount() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 $500");
		assertFalse(actual);
	}

	@Test
	public void checking_account_deposit_with_non_alphabet_characters_in_amount() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 500a");
		assertFalse(actual);
	}

	@Test
	public void savings_account_deposit_with_non_alphabet_characters_in_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 500a");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_checking_with_invalid_deposit_command_format() {
		boolean actual = depositCommandValidator.validate("deposit-23456789-500");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_saving_with_invalid_deposit_command_format() {
		boolean actual = depositCommandValidator.validate("deposit-12345678-500");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_saving_with_white_space_between_arguments() {
		boolean actual = depositCommandValidator.validate("deposit  12345678  500");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_saving_with_white_space_after_last_argument() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 500  ");
		assertTrue(actual);
	}

	@Test
	public void deposit_into_checking_with_white_space_between_arguments() {
		boolean actual = depositCommandValidator.validate("deposit  23456789  500");
		assertFalse(actual);
	}

	@Test
	public void deposit_into_checking_with_white_space_after_last_argument() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 500  ");
		assertTrue(actual);
	}

	@Test
	public void checking_account_deposit_with_negative_decimal_amount() {
		boolean actual = depositCommandValidator.validate("deposit 23456789 -100.50");
		assertFalse(actual);
	}

	@Test
	public void savings_account_deposit_with_negative_decimal_amount() {
		boolean actual = depositCommandValidator.validate("deposit 12345678 -100.50");
		assertFalse(actual);
	}

}
//commit