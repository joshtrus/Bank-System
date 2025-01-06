package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferCommandValidatorTest {
	public static final String SAVING_1 = "11111111";
	public static final String CHECKING_1 = "33333333";
	public static final String CD_1 = "88888888";
	public static final String SAVING_2 = "22222222";
	public static final String CHECKING_2 = "44444444";
	public static final String CD_2 = "99999999";
	TransferCommandValidator transferCommandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		transferCommandValidator = new TransferCommandValidator(bank);
		bank.addAccount(0.6, 0, SAVING_1, "savings");
		bank.addAccount(0.6, 0, CHECKING_1, "checking");
		bank.addAccount(0.6, 1500, CD_1, "cd");
		bank.addAccount(0.6, 1500, CD_2, "cd");
		bank.addAccount(0.6, 0, SAVING_2, "savings");
		bank.addAccount(0.6, 0, CHECKING_2, "checking");
	}

	@Test
	public void transfer_with_incorrect_argument_order() {
		boolean actual = transferCommandValidator.validate("transfer 500 11111111 22222222");
		assertFalse(actual);
	}

	@Test
	public void transfer_with_missing_source_account() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 500");
		assertFalse(actual);
	}

	@Test
	public void transfer_with_missing_destination_account() {
		boolean actual = transferCommandValidator.validate("transfer 22222222 500");
		assertFalse(actual);
	}

	@Test
	public void transfer_with_missing_amount() {
		boolean actual = transferCommandValidator.validate("transfer 22222222 11111111");
		assertFalse(actual);
	}

	@Test
	public void transfer_with_extra_argument_in_command() {
		boolean actual = transferCommandValidator.validate("transfer 22222222 11111111 500 43");
		assertFalse(actual);
	}

	@Test
	public void transfer_with_one_account_that_does_not_exist() {
		boolean actual = transferCommandValidator.validate("transfer 11113111 22222222 500");
		assertFalse(actual);
	}

	@Test
	public void transfer_with_two_accounts_that_do_not_exist() {
		boolean actual = transferCommandValidator.validate("transfer 11113111 22222322 500");
		assertFalse(actual);
	}

	@Test
	public void valid_savings_to_savings_transfer_command_with_existing_accounts() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 500");
		assertTrue(actual);
	}

	@Test
	public void valid_checkings_to_checkings_transfer_command_with_existing_accounts() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 300");
		assertTrue(actual);
	}

	@Test
	public void valid_checkings_to_savings_transfer_command_with_existing_accounts() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 300");
		assertTrue(actual);
	}

	@Test
	public void valid_savings_to_checkings_transfer_command_with_existing_accounts() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 300");
		assertTrue(actual);
	}

	@Test
	public void transfer_between_savings_and_cd_account_is_invalid() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 88888888 500");
		assertFalse(actual);
	}

	@Test
	public void transfer_between_checkings_and_cd_account_is_invalid() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 88888888 500");
		assertFalse(actual);
	}

	@Test
	public void transfer_between_cd_and_cd_account_is_invalid() {
		boolean actual = transferCommandValidator.validate("transfer 999999999 88888888 500");
		assertFalse(actual);
	}

	@Test
	public void transfer_between_non_numeric_source_id() {
		boolean actual = transferCommandValidator.validate("transfer 111111aq 22222222 500");
		assertFalse(actual);
	}

	@Test
	public void transfer_between_non_numeric_destination_id() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 222as222 500");
		assertFalse(actual);
	}

	@Test
	public void savings_to_savings_transfer_with_negative_amount() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 -500");
		assertFalse(actual);
	}

	@Test
	public void savings_to_checkings_transfer_with_negative_amount() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 -500");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_savings_transfer_with_negative_amount() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 -500");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_with_negative_amount() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 -500");
		assertFalse(actual);
	}

	@Test
	public void savings_to_savings_transfer_with_zero_amount() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 0");
		assertTrue(actual);
	}

	@Test
	public void savings_to_checkings_transfer_with_zero_amount() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 0");
		assertTrue(actual);
	}

	@Test
	public void checkings_to_savings_transfer_with_zero_amount() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 0");
		assertTrue(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_with_zero_amount() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 0");
		assertTrue(actual);
	}

	@Test
	public void savings_to_savings_transfer_with_non_numeric_amount() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void savings_to_checkings_transfer_with_non_numeric_amount() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_savings_transfer_with_non_numeric_amount() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_with_non_numeric_amount() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 fivehundred");
		assertFalse(actual);
	}

	@Test
	public void transfer_between_two_accounts_with_the_same_id() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 11111111 500");
		assertFalse(actual);
	}

	@Test
	public void savings_to_savings_transfer_of_amount_that_matches_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 2500");
		assertFalse(actual);
	}

	@Test
	public void savings_to_savings_transfer_of_amount_that_matches_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 1000");
		assertTrue(actual);
	}

	@Test
	public void savings_to_checkings_transfer_of_amount_that_matches_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 1000");
		assertTrue(actual);
	}

	@Test
	public void savings_to_checkings_transfer_of_amount_that_matches_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 1000");
		assertTrue(actual);
	}

	@Test
	public void checkings_to_savings_transfer_of_amount_that_matches_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 22222222 2500");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_savings_transfer_of_amount_that_matches_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 22222222 400");
		assertTrue(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_of_amount_that_matches_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 1000");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_of_amount_that_matches_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 400");
		assertTrue(actual);
	}

	@Test
	public void savings_to_savings_transfer_of_amount_that_exceeds_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 3000");
		assertFalse(actual);
	}

	@Test
	public void savings_to_savings_transfer_of_amount_that_exceeds_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 1500");
		assertFalse(actual);
	}

	@Test
	public void savings_to_checkings_transfer_of_amount_that_exceeds_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 1500");
		assertFalse(actual);
	}

	@Test
	public void savings_to_checkings_transfer_of_amount_that_exceeds_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 1500");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_savings_transfer_of_amount_that_exceeds_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 3000");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_savings_transfer_of_amount_that_exceeds_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 1500");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_of_amount_that_exceeds_destination_account_max_deposit_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 1500");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_of_amount_that_exceeds_source_account_max_withdraw_limit() {
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 1500");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_twice_in_one_month() {
		bank.addDollars(600, "33333333");
		bank.transferDollars("33333333", "44444444", 100);
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 300");
		assertTrue(actual);
	}

	@Test
	public void checkings_to_savings_transfer_twice_in_one_month() {
		bank.addDollars(600, "33333333");
		bank.transferDollars("33333333", "11111111", 100);
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 300");
		assertTrue(actual);
	}

	@Test
	public void savings_to_checkings_transfer_twice_in_one_month() {
		bank.addDollars(600, "11111111");
		bank.transferDollars("11111111", "33333333", 100);
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 300");
		assertFalse(actual);
	}

	@Test
	public void savings_to_savings_transfer_twice_in_one_month() {
		bank.addDollars(600, "11111111");
		bank.transferDollars("11111111", "22222222", 100);
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 300");
		assertFalse(actual);
	}

	@Test
	public void checkings_to_checkings_transfer_twice_in_different_months() {
		bank.addDollars(600, "33333333");
		bank.transferDollars("33333333", "44444444", 100);
		bank.passTime(1);
		boolean actual = transferCommandValidator.validate("transfer 33333333 44444444 300");
		assertTrue(actual);
	}

	@Test
	public void checkings_to_savings_transfer_twice_in_different_months() {
		bank.addDollars(600, "33333333");
		bank.transferDollars("33333333", "11111111", 100);
		bank.passTime(1);
		boolean actual = transferCommandValidator.validate("transfer 33333333 11111111 300");
		assertTrue(actual);
	}

	@Test
	public void savings_to_checkings_transfer_twice_in_different_months() {
		bank.addDollars(600, "11111111");
		bank.transferDollars("11111111", "33333333", 100);
		bank.passTime(1);
		boolean actual = transferCommandValidator.validate("transfer 11111111 33333333 300");
		assertTrue(actual);
	}

	@Test
	public void savings_to_savings_transfer_twice_in_different_months() {
		bank.addDollars(600, "11111111");
		bank.transferDollars("11111111", "22222222", 100);
		bank.passTime(1);
		boolean actual = transferCommandValidator.validate("transfer 11111111 22222222 300");
		assertTrue(actual);
	}

	@Test
	public void cd_to_cd_transfer_invalid_after_12_months() {
		bank.passTime(12);
		boolean actual = transferCommandValidator.validate("transfer 88888888 99999999 300");
		assertFalse(actual);
	}

}
