package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateCommandValidatorTest {
	public static final String ID_1 = "12345678";
	public static final double APR = 3.5;
	public static final int DOLLARS = 200;

	CreateCommandValidator createCommandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		createCommandValidator = new CreateCommandValidator(bank);
	}

	@Test
	public void creating_an_account_with_an_invalid_account_type() {
		boolean actual = createCommandValidator.validate("Create investment 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_an_account_with_no_arguments() {
		boolean actual = createCommandValidator.validate("create ");
		assertFalse(actual);
	}

	@Test
	public void creating_account_with_missing_account_type_and_apr() {
		boolean actual = createCommandValidator.validate("Create 12345678");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_only_account_type_provided() {
		boolean actual = createCommandValidator.validate("Create savings");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_only_account_type_provided() {
		boolean actual = createCommandValidator.validate("Create checking");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_only_account_type_provided() {
		boolean actual = createCommandValidator.validate("Create cd");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_an_id_that_already_exists_in_bank() {
		bank.addAccount(APR, DOLLARS, ID_1, "savings");
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_an_id_that_already_exists_in_bank() {
		bank.addAccount(APR, DOLLARS, ID_1, "checking");
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_CD_account_with_an_id_that_already_exists_in_bank() {
		bank.addAccount(APR, DOLLARS, ID_1, "cd");
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6 2000");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_an_id_that_doesnt_already_exist_in_bank() {
		bank.addAccount(APR, DOLLARS, ID_1, "savings");
		boolean actual = createCommandValidator.validate("Create savings 23456789 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_an_id_that_doesnt_already_exist_in_bank() {
		bank.addAccount(APR, DOLLARS, ID_1, "checking");
		boolean actual = createCommandValidator.validate("Create checking 23456789 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_CD_account_with_an_id_that_doesnt_already_exist_in_bank() {
		bank.addAccount(APR, DOLLARS, ID_1, "banking.CD");
		boolean actual = createCommandValidator.validate("Create cd 23456789 0.6 2000");
		assertTrue(actual);
	}

	@Test
	public void creating_a_valid_checking_account() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_valid_savings_account() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_valid_CD_account_with_minimum_balance() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6 1000");
		assertTrue(actual);
	}

	@Test
	public void creating_a_valid_CD_account_with_maximum_balance() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6 10000");
		assertTrue(actual);
	}

	@Test
	public void creating_a_CD_account_with_no_apr_value() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_no_apr_value() {
		boolean actual = createCommandValidator.validate("Create savings 12345678");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_no_apr_value() {
		boolean actual = createCommandValidator.validate("Create checking 12345678");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_negative_apr_value() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 -0.1");
		assertFalse(actual);
	}

	@Test
	public void creating_a_CD_account_with_negative_apr_value() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 -0.1 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_negative_apr_value() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 -0.1");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_apr_value_over_10_percent() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 11.5");
		assertFalse(actual);
	}

	@Test
	public void creating_a_saving_account_with_apr_value_over_10_percent() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 11.5");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_apr_value_over_10_percent() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 11.5 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_non_numerical_apr_value() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 abc 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_non_numerical_apr_value() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 abc");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_non_numerical_apr_value() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 abc");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_a_valid_apr_value() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.5");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_a_valid_apr_value() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.5");
		assertTrue(actual);
	}

	@Test
	public void creating_a_CD_account_with_a_valid_apr_value() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.5 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_savings_account_with_an_id_less_than_eight_digits() {
		boolean actual = createCommandValidator.validate("Create savings 123478 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_an_id_less_than_eight_digits() {
		boolean actual = createCommandValidator.validate("Create checking 123478 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_an_id_less_than_eight_digits() {
		boolean actual = createCommandValidator.validate("Create cd 123478 0.6 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_an_id_more_than_eight_digits() {
		boolean actual = createCommandValidator.validate("Create cd 123456789 0.6 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_an_id_more_than_eight_digits() {
		boolean actual = createCommandValidator.validate("Create savings 123456789 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_an_id_more_than_eight_digits() {
		boolean actual = createCommandValidator.validate("Create checking 123456789 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_a_non_numerical_id() {
		boolean actual = createCommandValidator.validate("Create checking abcdefgh 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_a_non_numerical_id() {
		boolean actual = createCommandValidator.validate("Create savings abcdefgh 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_a_non_numerical_id() {
		boolean actual = createCommandValidator.validate("Create cd abcdefgh 0.6 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_a_valid_id() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_savings_account_with_a_valid_id() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_a_valid_id() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_incorrect_spelling() {
		boolean actual = createCommandValidator.validate("Create chequeings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_incorrect_spelling() {
		boolean actual = createCommandValidator.validate("Create saveings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_incorrect_spelling() {
		boolean actual = createCommandValidator.validate("Create ceedee 12345678 0.6 5000");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_multiple_upper_cases() {
		boolean actual = createCommandValidator.validate("Create saViNgs 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_multiple_upper_cases() {
		boolean actual = createCommandValidator.validate("Create CHEcking 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_cd_account_with_multiple_upper_cases() {
		boolean actual = createCommandValidator.validate("Create cD 12345678 0.6 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_cd_account_with_missing_account_type() {
		boolean actual = createCommandValidator.validate("Create 12345678 0.6 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_missing_account_type() {
		boolean actual = createCommandValidator.validate("Create 12345678 0.6 ");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_missing_account_type() {
		boolean actual = createCommandValidator.validate("Create 12345678 0.6 ");
		assertFalse(actual);
	}

	@Test
	public void creating_cd_account_with_balance_below_minimum() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6 100");
		assertFalse(actual);
	}

	@Test
	public void creating_cd_account_with_balance_above_maximum() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6 11000");
		assertFalse(actual);
	}

	@Test
	public void creating_cd_account_without_balance() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_extra_arguments_after_valid_command() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.6 extra");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_extra_arguments_after_valid_command() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.6 extra");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_extra_arguments_after_valid_command() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6 1500 extra");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_extra_white_space_between_arguments() {
		boolean actual = createCommandValidator.validate("Create  savings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_extra_white_space_between_arguments() {
		boolean actual = createCommandValidator.validate("Create  checking 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_extra_white_space_between_arguments() {
		boolean actual = createCommandValidator.validate("Create  cd 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_an_id_containing_special_characters() {
		boolean actual = createCommandValidator.validate("Create savings 12%45@78 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_an_id_containing_special_characters() {
		boolean actual = createCommandValidator.validate("Create checking 12%45@78 0.6");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_an_id_containing_special_characters() {
		boolean actual = createCommandValidator.validate("Create cd 12%45@78 0.6 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_an_apr_containing_percent_sign() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.6%");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_an_apr_containing_percent_sign() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.6%");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_an_apr_containing_percent_sign() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.6% 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_id_with_leading_zeros() {
		boolean actual = createCommandValidator.validate("Create savings 01234567 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_id_with_leading_zeros() {
		boolean actual = createCommandValidator.validate("Create checking 01234567 0.6");
		assertTrue(actual);
	}

	@Test
	public void creating_a_cd_account_with_id_with_leading_zeros() {
		boolean actual = createCommandValidator.validate("Create cd 01234567 0.6 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_savings_account_with_apr_exactly_ten() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 10");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_apr_exactly_ten() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 10");
		assertTrue(actual);
	}

	@Test
	public void creating_a_cd_account_with_apr_exactly_ten() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 10 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_cd_account_with_apr_exactly_zero() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_saving_account_with_apr_exactly_zero() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_apr_exactly_zero() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_decimal_at_max_value() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 9.99");
		assertTrue(actual);
	}

	@Test
	public void creating_a_savings_account_with_decimal_at_max_value() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 9.99");
		assertTrue(actual);
	}

	@Test
	public void creating_a_cd_account_with_decimal_at_max_value() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 9.99 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_cd_account_with_decimal_at_min_value() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.01 1500");
		assertTrue(actual);
	}

	@Test
	public void creating_a_checking_account_with_decimal_at_min_value() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.01");
		assertTrue(actual);
	}

	@Test
	public void creating_a_savings_account_with_decimal_at_min_value() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.01");
		assertTrue(actual);
	}

	@Test
	public void creating_a_savings_account_with_no_spaces_between_arguments() {
		boolean actual = createCommandValidator.validate("Createsavings123456780.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_no_spaces_between_arguments() {
		boolean actual = createCommandValidator.validate("Createchecking123456780.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_no_spaces_between_arguments() {
		boolean actual = createCommandValidator.validate("Createcd123456780.011500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_spaces_between_the_id() {
		boolean actual = createCommandValidator.validate("Create savings 123 45678 0.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_spaces_between_the_id() {
		boolean actual = createCommandValidator.validate("Create checking 123 45678 0.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_spaces_between_the_id() {
		boolean actual = createCommandValidator.validate("Create cd 123 45678 0.01 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_spaces_between_the_apr() {
		boolean actual = createCommandValidator.validate("Create checking 12345678 0.0 1");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_spaces_between_the_apr() {
		boolean actual = createCommandValidator.validate("Create savings 12345678 0.0 1");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_spaces_between_the_apr() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.0 1 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_spaces_between_the_balance() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.01 15 00");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_alphanumeric_account_type() {
		boolean actual = createCommandValidator.validate("Create sav1ngs 12345678 0.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_alphanumeric_account_type() {
		boolean actual = createCommandValidator.validate("Create check1ngs 12345678 0.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_alphanumeric_account_type() {
		boolean actual = createCommandValidator.validate("Create 3cd 12345678 0.01 1500");
		assertFalse(actual);
	}

	@Test
	public void creating_a_checking_account_with_special_characters_in_account_type() {
		boolean actual = createCommandValidator.validate("Create check!ngs 12345678 0.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_savings_account_with_special_characters_in_account_type() {
		boolean actual = createCommandValidator.validate("Create sav!ngs 12345678 0.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_special_characters_in_account_type() {
		boolean actual = createCommandValidator.validate("Create cd# 12345678 0.01");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_balance_just_below_min() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.01 999");
		assertFalse(actual);
	}

	@Test
	public void creating_a_cd_account_with_balance_just_above_max() {
		boolean actual = createCommandValidator.validate("Create cd 12345678 0.01 10001");
		assertFalse(actual);
	}

}
