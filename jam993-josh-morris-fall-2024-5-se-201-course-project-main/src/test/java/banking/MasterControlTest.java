package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MasterControlTest {
	MasterControl masterControl;
	private List<String> input;

	private void assertSingleCommand(String command, List<String> actual) {
		assertEquals(1, actual.size());
		assertEquals(command, actual.get(0));
	}

	private void assertDoubleCommand(String command1, String command2, List<String> actual) {
		assertEquals(2, actual.size());
		assertEquals(command1, actual.get(0));
		assertEquals(command2, actual.get(1));
	}

	@BeforeEach
	void setUp() {
		input = new ArrayList<>();
		Bank bank = new Bank();
		masterControl = new MasterControl(new CommandValidator(bank), new CommandProcessor(bank), new CommandStorage(),
				new TransactionHistory(bank));
	}

	@Test
	void incorrect_spelling_for_create_command_is_invaid() {
		input.add("crieate checking 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("crieate checking 12345678 1.0", actual);
	}

	@Test
	void incorrect_spelling_for_deposit_command_is_invaid() {
		input.add("depositt 12345678 100");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("depositt 12345678 100", actual);
	}

	@Test
	void two_incorrect_spelling_commands_both_are_invalid() {
		input.add("depositt 12345678 100");
		input.add("crieate checking 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(2, actual.size());
		assertEquals("depositt 12345678 100", actual.get(0));
		assertEquals("crieate checking 12345678 1.0", actual.get(1));
	}

	@Test
	void creating_two_accounts_with_the_same_id_is_invalid() {
		input.add("create checking 12345678 1.0");
		input.add("create checking 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(2, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("create checking 12345678 1.0", actual.get(1));

	}

	@Test
	void typo_in_checking_account_type_is_invalid() {
		input.add("create chcking 12345678 1.0");
		List<String> actual = masterControl.start(input);
		assertSingleCommand("create chcking 12345678 1.0", actual);

	}

	@Test
	void typo_in_savings_account_type_is_invalid() {
		input.add("create svings 12345678 1.0");
		List<String> actual = masterControl.start(input);
		assertSingleCommand("create svings 12345678 1.0", actual);
	}

	@Test
	void typo_in_cd_account_type_is_invalid() {
		input.add("create ceedee 12345678 1.0 1500");
		List<String> actual = masterControl.start(input);
		assertSingleCommand("create ceedee 12345678 1.0 1500", actual);
	}

	@Test
	void creating_cd_account_without_starting_balance_invalid() {
		input.add("create cd 12345678 1.0");
		List<String> actual = masterControl.start(input);
		assertSingleCommand("create cd 12345678 1.0", actual);
	}

	@Test
	void cannot_deposit_into_account_that_does_not_exist() {
		input.add("create checking 12345678 1.0");
		input.add("deposit 23456789 500");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 0.00 1.00", "deposit 23456789 500", actual);
	}

	@Test
	void cannot_deposit_into_cd_account() {
		input.add("create cd 12345678 1.0 1500");
		input.add("deposit 12345678 500");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Cd 12345678 1500.00 1.00", "deposit 12345678 500", actual);
	}

	@Test
	void cannot_deposit_more_than_max_amount_into_checking_accounts() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 1001");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 0.00 0.60", "deposit 12345678 1001", actual);
	}

	@Test
	void cannot_deposit_more_than_max_amount_into_savings_accounts() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 2501");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Savings 12345678 0.00 0.60", "deposit 12345678 2501", actual);
	}

	@Test
	void cannot_deposit_negative_number_into_checking() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 -100");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 0.00 0.60", "deposit 12345678 -100", actual);
	}

	@Test
	void cannot_deposit_negative_number_into_savings() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 -100");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Savings 12345678 0.00 0.60", "deposit 12345678 -100", actual);

	}

	@Test
	void cannot_deposit_non_numeric_amount_into_savings() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 abc");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Savings 12345678 0.00 0.60", "deposit 12345678 abc", actual);

	}

	@Test
	void cannot_deposit_non_numeric_amount_into_checking() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 abc");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 0.00 0.60", "deposit 12345678 abc", actual);
	}

	@Test
	void cannot_create_checking_account_with_id_not_an_eight_digit_number() {
		input.add("create checking 1234567 0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 1234567 0.01", actual);
	}

	@Test
	void cannot_create_savings_account_with_id_not_an_eight_digit_number() {
		input.add("create savings 1234567 0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings 1234567 0.01", actual);
	}

	@Test
	void cannot_create_checking_account_with_alphanumeric_id() {
		input.add("create checking 123f4567 0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 123f4567 0.01", actual);
	}

	@Test
	void cannot_create_savings_account_with_alphanumeric_id() {
		input.add("create savings 123f4567 0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings 123f4567 0.01", actual);
	}

	@Test
	void cannot_create_checking_account_with_alphabetic_id() {
		input.add("create checking abcdefgh 0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking abcdefgh 0.01", actual);
	}

	@Test
	void cannot_create_savings_account_with_alphabetic_id() {
		input.add("create savings abcdefgh 0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings abcdefgh 0.01", actual);
	}

	@Test
	void cannot_create_checking_account_with_negative_apr() {
		input.add("create checking 12345678 -0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 12345678 -0.01", actual);
	}

	@Test
	void cannot_create_savings_account_with_negative_apr() {
		input.add("create savings 12345678 -0.01");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings 12345678 -0.01", actual);
	}

	@Test
	void cannot_create_cd_account_with_negative_apr() {
		input.add("create cd 12345678 -0.01 5000");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 12345678 -0.01 5000", actual);
	}

	@Test
	void cannot_create_cd_account_with_apr_over_limit() {
		input.add("create cd 12345678 11 5000");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 12345678 11 5000", actual);

	}

	@Test
	void cannot_create_savings_account_with_apr_over_limit() {
		input.add("create savings 12345678 11");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings 12345678 11", actual);

	}

	@Test
	void cannot_create_checking_account_with_apr_over_limit() {
		input.add("create checking 12345678 11");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 12345678 11", actual);

	}

	@Test
	void cannot_create_cd_acount_with_negative_balance() {
		input.add("create cd 12345678 1 -5000");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 12345678 1 -5000", actual);
	}

	@Test
	void cannot_create_cd_acount_with_zero_balance() {
		input.add("create cd 12345678 1 0");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 12345678 1 0", actual);
	}

	@Test
	void cannot_create_cd_acount_with_non_numeric_balance() {
		input.add("create cd 12345678 1 ab23");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 12345678 1 ab23", actual);
	}

	@Test
	void cannot_create_checking_account_with_extra_arguments() {
		input.add("create checking 12345678 0.01 extra");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 12345678 0.01 extra", actual);
	}

	@Test
	void cannot_create_savings_account_with_extra_arguments() {
		input.add("create savings 12345678 0.01 extra");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings 12345678 0.01 extra", actual);
	}

	@Test
	void cannot_create_cd_account_with_extra_arguments() {
		input.add("create cd 12345678 0.01 5000 extra");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 12345678 0.01 5000 extra", actual);
	}

	@Test
	void cannot_create_savings_account_with_missing_apr() {
		input.add("create savings 12345678");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings 12345678", actual);
	}

	@Test
	void cannot_create_checking_account_with_missing_apr() {
		input.add("create checking 12345678");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 12345678", actual);
	}

	@Test
	void cannot_create_cd_account_with_missing_apr() {
		input.add("create cd 12345678 5000");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 12345678 5000", actual);
	}

	@Test
	void cannot_create_cd_account_with_missing_id() {
		input.add("create cd 0.5 5000");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create cd 0.5 5000", actual);
	}

	@Test
	void cannot_create_savings_account_with_missing_id() {
		input.add("create savings 0.5");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create savings 0.5", actual);
	}

	@Test
	void cannot_create_checking_account_with_missing_id() {
		input.add("create checking 0.5");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 0.5", actual);
	}

	@Test
	void cannot_create_account_with_missing_type() {
		input.add("create 12345678 0.5");
		List<String> actual = masterControl.start(input);
		assertSingleCommand("create 12345678 0.5", actual);
	}

	@Test
	void cannot_create_account_with_unsupported_type() {
		input.add("create investment 12345678 0.5");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create investment 12345678 0.5", actual);
	}

	@Test
	void cannot_deposit_with_missing_amount() {
		input.add("deposit 12345678");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("deposit 12345678", actual);
	}

	@Test
	void cannot_deposit_with_missing_id() {
		input.add("deposit 500");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("deposit 500", actual);
	}

	@Test
	void cannot_deposit_with_extra_argument_in_command() {
		input.add("deposit 12345678 500 extra");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("deposit 12345678 500 extra", actual);
	}

	@Test
	void cannot_deposit_with_non_numeric_amount() {
		input.add("deposit 12345678 $500");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("deposit 12345678 $500", actual);
	}

	@Test
	void cannot_deposit_with_non_numeric_id() {
		input.add("deposit 12345@678 500");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("deposit 12345@678 500", actual);
	}

	@Test
	void in_sequence_of_valid_and_invalid_commands_valid_commands_do_not_process() {
		input.add("create savings 12345678 0.6");
		input.add("create savings 22222222 0.6 extra");
		input.add("create checking 34567890 0.6");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());
		assertEquals("Savings 12345678 0.00 0.60", actual.get(0));
		assertEquals("Checking 34567890 0.00 0.60", actual.get(1));
		assertEquals("create savings 22222222 0.6 extra", actual.get(2));
	}

	@Test
	void valid_create_savings_account_command() {
		input.add("create savings 12345678 0.6");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Savings 12345678 0.00 0.60", actual);
	}

	@Test
	void valid_create_cd_account_command() {
		input.add("create cd 12345678 0.6 1500");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Cd 12345678 1500.00 0.60", actual);
	}

	@Test
	void valid_create_checking_account_command() {
		input.add("create checking 12345678 0.6");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Checking 12345678 0.00 0.60", actual);
	}

	@Test
	void creating_multiple_unique_accounts_is_valid() {
		input.add("create checking 12345678 0.6");
		input.add("create savings 23456789 0.6");
		input.add("create cd 34567890 0.6 1500");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 0.00 0.60", actual.get(0));
		assertEquals("Savings 23456789 0.00 0.60", actual.get(1));
		assertEquals("Cd 34567890 1500.00 0.60", actual.get(2));
	}

	@Test
	void creating_cd_account_with_min_balance_is_valid() {
		input.add("create cd 12345678 0.6 1000");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Cd 12345678 1000.00 0.60", actual);
	}

	@Test
	void creating_cd_account_with_max_balance_is_valid() {
		input.add("create cd 12345678 0.6 10000");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Cd 12345678 10000.00 0.60", actual);
	}

	@Test
	void creating_cd_account_with_min_apr_is_valid() {
		input.add("create cd 12345678 0 1500");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Cd 12345678 1500.00 0.00", actual);
	}

	@Test
	void creating_savings_account_with_min_apr_is_valid() {
		input.add("create savings 12345678 0");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Savings 12345678 0.00 0.00", actual);
	}

	@Test
	void creating_checking_account_with_min_apr_is_valid() {
		input.add("create checking 12345678 0");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Checking 12345678 0.00 0.00", actual);
	}

	@Test
	void creating_cd_account_with_max_apr_is_valid() {
		input.add("create cd 12345678 10 1500");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Cd 12345678 1500.00 10.00", actual);
	}

	@Test
	void creating_savings_account_with_max_apr_is_valid() {
		input.add("create savings 12345678 10");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Savings 12345678 0.00 10.00", actual);
	}

	@Test
	void creating_checking_account_with_max_apr_is_valid() {
		input.add("create checking 12345678 10");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Checking 12345678 0.00 10.00", actual);
	}

	@Test
	void valid_savings_account_deposit() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 600");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Savings 12345678 600.00 0.60", "Deposit 12345678 600", actual);
	}

	@Test
	void valid_checking_account_deposit() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 600");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 600.00 0.60", "Deposit 12345678 600", actual);

	}

	@Test
	void valid_checking_account_deposit_at_lower_limit() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 0");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 0.00 0.60", "Deposit 12345678 0", actual);
	}

	@Test
	void valid_savings_account_deposit_at_lower_limit() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 0");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Savings 12345678 0.00 0.60", "Deposit 12345678 0", actual);
	}

	@Test
	void valid_savings_account_deposit_at_upper_limit() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 2500");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Savings 12345678 2500.00 0.60", "Deposit 12345678 2500", actual);

	}

	@Test
	void valid_checking_account_deposit_at_upper_limit() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 1000");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 1000.00 0.60", "Deposit 12345678 1000", actual);
	}

	@Test
	void multiple_deposits_to_savings_account_at_once_is_valid() {
		input.add("create savings 12345678 0.6");
		input.add("deposit 12345678 500");
		input.add("deposit 12345678 600");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());

		assertEquals("Savings 12345678 1100.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Deposit 12345678 600", actual.get(2));
	}

	@Test
	void multiple_deposits_to_checking_account_at_once_is_valid() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 500");
		input.add("deposit 12345678 600");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());

		assertEquals("Checking 12345678 1100.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Deposit 12345678 600", actual.get(2));
	}

	@Test
	void multiple_deposits_to_different_accounts_at_once_is_valid() {
		input.add("create checking 12345678 0.6");
		input.add("create savings 23456789 0.6");
		input.add("deposit 12345678 500");
		input.add("deposit 23456789 600");
		List<String> actual = masterControl.start(input);

		assertEquals(4, actual.size());
		assertEquals("Checking 12345678 500.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Savings 23456789 600.00 0.60", actual.get(2));
		assertEquals("Deposit 23456789 600", actual.get(3));
	}

	@Test
	void typo_in_withdraw_command_is_invalid() {
		input.add("create checking 12345678 0.6");
		input.add("Withedraw 12345678 0");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 0.00 0.60", "Withedraw 12345678 0", actual);
	}

	@Test
	void withdraw_from_account_that_doesnt_exist_invalid() {
		input.add("create checking 12345678 0.6");
		input.add("Withdraw 22222222 500");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Checking 12345678 0.00 0.60", "Withdraw 22222222 500", actual);
	}

	@Test
	void withdraw_from_closed_account_is_invalid() {
		input.add("create checking 12345678 0.6");
		input.add("pass 2");
		input.add("Withdraw 12345678 500");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("Withdraw 12345678 500", actual);
	}

	@Test
	void withdraw_from_cd_account_before_twelve_months_invalid() {
		input.add("create cd 12345678 0.6 1500");
		input.add("Withdraw 12345678 500");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Cd 12345678 1500.00 0.60", "Withdraw 12345678 500", actual);
	}

	@Test
	void multiple_withdraws_from_savings_account_in_same_month_invalid() {
		input.add("create savings 12345678 0.6");
		input.add("Deposit 12345678 2000");
		input.add("Withdraw 12345678 500");
		input.add("Withdraw 12345678 600");

		List<String> actual = masterControl.start(input);

		assertEquals(4, actual.size());
		assertEquals("Savings 12345678 1500.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 2000", actual.get(1));
		assertEquals("Withdraw 12345678 500", actual.get(2));
		assertEquals("Withdraw 12345678 600", actual.get(3));
	}

	@Test
	void withdraw_from_savings_account_within_limit_valid() {
		input.add("create savings 12345678 0.6");
		input.add("Deposit 12345678 500");
		input.add("Withdraw 12345678 100");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());
		assertEquals("Savings 12345678 400.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Withdraw 12345678 100", actual.get(2));
	}

	@Test
	void withdraw_from_checking_account_within_limit_valid() {
		input.add("create checking 12345678 0.6");
		input.add("Deposit 12345678 500");
		input.add("Withdraw 12345678 100");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 400.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 500", actual.get(1));
		assertEquals("Withdraw 12345678 100", actual.get(2));
	}

	@Test
	void withdraw_from_cd_account_after_twelve_months_valid() {
		input.add("create cd 12345678 0.6 1500");
		input.add("pass 12");
		input.add("Withdraw 12345678 2000");

		List<String> actual = masterControl.start(input);

		assertDoubleCommand("Cd 12345678 0.00 0.60", "Withdraw 12345678 2000", actual);
	}

	@Test
	void multiple_withdraw_from_different_accounts_in_same_month() {
		input.add("create checking 11111111 0.6");
		input.add("create savings 22222222 0.6");
		input.add("Deposit 11111111 1000");
		input.add("Deposit 22222222 2500");
		input.add("Withdraw 22222222 500");
		input.add("Withdraw 22222222 600");
		input.add("Withdraw 11111111 200");
		input.add("Withdraw 11111111 300");

		List<String> actual = masterControl.start(input);

		assertEquals(8, actual.size());
		assertEquals("Checking 11111111 500.00 0.60", actual.get(0));
		assertEquals("Deposit 11111111 1000", actual.get(1));
		assertEquals("Withdraw 11111111 200", actual.get(2));
		assertEquals("Withdraw 11111111 300", actual.get(3));
		assertEquals("Savings 22222222 2000.00 0.60", actual.get(4));
		assertEquals("Deposit 22222222 2500", actual.get(5));
		assertEquals("Withdraw 22222222 500", actual.get(6));
		assertEquals("Withdraw 22222222 600", actual.get(7));

	}

	@Test
	void withdraw_over_period_of_months() {
		input.add("create savings 12345678 0.6");
		input.add("Deposit 12345678 1000");
		input.add("Withdraw 12345678 300");
		input.add("pass 1");
		input.add("Withdraw 12345678 400");

		List<String> actual = masterControl.start(input);

		assertEquals(4, actual.size());
		assertEquals("Savings 12345678 300.35 0.60", actual.get(0));
		assertEquals("Deposit 12345678 1000", actual.get(1));
		assertEquals("Withdraw 12345678 300", actual.get(2));
		assertEquals("Withdraw 12345678 400", actual.get(3));
	}

	@Test
	void typo_in_transfer_command_is_invalid() {
		input.add("Transefer 11111111 22222222 300");

		List<String> actual = masterControl.start(input);
		assertSingleCommand("Transefer 11111111 22222222 300", actual);
	}

	@Test
	void multiple_transfers_at_a_time_between_checking_is_valic() {
		input.add("create checking 12345678 0.6");
		input.add("create checking 23456789 0.6");
		input.add("Deposit 12345678 1000");
		input.add("Transfer 12345678 23456789 300");
		input.add("Transfer 23456789 12345678 200");

		List<String> actual = masterControl.start(input);
		assertEquals(7, actual.size());
		assertEquals("Checking 12345678 900.00 0.60", actual.get(0));
		assertEquals("Deposit 12345678 1000", actual.get(1));
		assertEquals("Transfer 12345678 23456789 300", actual.get(2));
		assertEquals("Transfer 23456789 12345678 200", actual.get(3));
		assertEquals("Checking 23456789 100.00 0.60", actual.get(4));
		assertEquals("Transfer 12345678 23456789 300", actual.get(5));
		assertEquals("Transfer 23456789 12345678 200", actual.get(6));

	}

	@Test
	void pass_time_with_three_unique_accounts_and_subtract_from_accounts_under_100_balance() {
		input.add("create checking 12345678 1.50");
		input.add("deposit 12345678 80");
		input.add("create savings 87654321 1.50");
		input.add("deposit 87654321 2500");
		input.add("deposit 87654321 2500");
		input.add("create checking 23456789 0.6");
		input.add("create cd 98765432 1 5000");
		input.add("pass 1");

		List<String> actual = masterControl.start(input);

		assertEquals(6, actual.size());
		assertEquals("Checking 12345678 55.06 1.50", actual.get(0));
		assertEquals("Deposit 12345678 80", actual.get(1));
		assertEquals("Savings 87654321 5006.25 1.50", actual.get(2));
		assertEquals("Deposit 87654321 2500", actual.get(3));
		assertEquals("Deposit 87654321 2500", actual.get(4));
		assertEquals("Cd 98765432 5016.66 1.00", actual.get(5));
	}

	@Test
	void creating_account_with_id_from_previously_closed_account() {
		input.add("create checking 12345678 0.6");
		input.add("deposit 12345678 100");
		input.add("create savings 87654321 0.6");
		input.add("pass 1");
		input.add("create cd 87654321 0.6 10000");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 100.05 0.60", actual.get(0));
		assertEquals("Deposit 12345678 100", actual.get(1));
		assertEquals("Cd 87654321 10000.00 0.60", actual.get(2));
	}

	@Test
	void sample_make_sure_this_passes_unchanged_or_you_will_fail() {
		input.add("Create savings 12345678 0.6");
		input.add("Deposit 12345678 700");
		input.add("Deposit 12345678 5000");
		input.add("creAte cHecKing 98765432 0.01");
		input.add("Deposit 98765432 300");
		input.add("Transfer 98765432 12345678 300");
		input.add("Pass 1");
		input.add("Create cd 23456789 1.2 2000");

		List<String> actual = masterControl.start(input);

		assertEquals(5, actual.size());
		assertEquals("Savings 12345678 1000.50 0.60", actual.get(0));
		assertEquals("Deposit 12345678 700", actual.get(1));
		assertEquals("Transfer 98765432 12345678 300", actual.get(2));
		assertEquals("Cd 23456789 2000.00 1.20", actual.get(3));
		assertEquals("Deposit 12345678 5000", actual.get(4));
	}

}
