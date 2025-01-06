package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandValidatorTest {
	CommandValidator commandValidator;
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		commandValidator = new CommandValidator(bank);
		bank.addAccount(0.6, 0, "23456789", "savings");
		bank.addAccount(0.6, 0, "55555555", "checking");
	}

	@Test
	public void empty_command() {
		boolean actual = commandValidator.validate(" ");
		assertFalse(actual);
	}

	@Test
	public void missing_type_of_command() {
		boolean actual = commandValidator.validate("Savings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void type_of_command_does_not_exist() {
		boolean actual = commandValidator.validate("Make savings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void create_command_for_savings_is_a_valid_command() {
		boolean actual = commandValidator.validate("Create savings 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void create_command_for_checking_is_a_valid_command() {
		boolean actual = commandValidator.validate("Create checking 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void create_command_for_cd_is_a_valid_command() {
		boolean actual = commandValidator.validate("Create cd 12345678 0.6 1500");
		assertTrue(actual);
	}

	@Test
	public void first_case_in_create_command_is_not_upper() {
		boolean actual = commandValidator.validate("create savings 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void multiple_characters_in_create_command_are_upper() {
		boolean actual = commandValidator.validate("CrEaTe savings 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void first_case_in_create_command_is_upper() {
		boolean actual = commandValidator.validate("Create savings 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void incorrect_spelling_of_command_type() {
		boolean actual = commandValidator.validate("Crieate savings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void deposit_command_is_a_valid_command() {
		boolean actual = commandValidator.validate("Deposit 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void first_case_in_deposit_command_is_not_upper() {
		boolean actual = commandValidator.validate("deposit 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void multiple_characters_in_deposit_command_are_upper() {
		boolean actual = commandValidator.validate("DePOSiT 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void first_case_in_deposit_command_is_upper() {
		boolean actual = commandValidator.validate("Deposit 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void incorrect_spelling_of_deposit_command_type() {
		boolean actual = commandValidator.validate("Depiosit 12345678 500");
		assertFalse(actual);
	}

	@Test
	public void alphanumeric_deposit_command_used() {
		boolean actual = commandValidator.validate("D3pos1t 12345678 500");
		assertFalse(actual);
	}

	@Test
	public void alphanumeric_create_command_used() {
		boolean actual = commandValidator.validate("cr3at4 savings 12345678 0.6");
		assertFalse(actual);
	}

	@Test
	public void valid_withdraw_command() {
		boolean actual = commandValidator.validate("withdraw 23456789 0.6");
		assertTrue(actual);
	}

	@Test
	public void withdraw_command_is_a_valid_command() {
		boolean actual = commandValidator.validate("Withdraw 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void first_case_in_withdraw_command_is_not_upper() {
		boolean actual = commandValidator.validate("withdraw 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void multiple_characters_in_withdraw_command_are_upper() {
		boolean actual = commandValidator.validate("WiTHDraW 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void first_case_in_withdraw_command_is_upper() {
		boolean actual = commandValidator.validate("Withdraw 23456789 500");
		assertTrue(actual);
	}

	@Test
	public void incorrect_spelling_of_withdraw_command_type() {
		boolean actual = commandValidator.validate("Withedraw 12345678 500");
		assertFalse(actual);
	}

	@Test
	public void alphanumeric_withdraw_command_used() {
		boolean actual = commandValidator.validate("W1thdr4w 12345678 500");
		assertFalse(actual);
	}

	@Test
	public void incorrect_spelling_of_transfer_command_type() {
		boolean actual = commandValidator.validate("Transfir 23456789 55555555 500");
		assertFalse(actual);
	}

	@Test
	public void valid_transfer_command_between_two_accounts() {
		boolean actual = commandValidator.validate("Transfer 23456789 55555555 500");
		assertTrue(actual);
	}

	@Test
	public void alphanumeric_spelling_of_transfer_command_type() {
		boolean actual = commandValidator.validate("transf3r 23456789 55555555 500");
		assertFalse(actual);
	}

	@Test
	public void characters_in_transfer_command_are_all_upper() {
		boolean actual = commandValidator.validate("TRANSFER 23456789 55555555 500");
		assertTrue(actual);
	}

	@Test
	public void characters_in_transfer_command_are_upper_and_lower() {
		boolean actual = commandValidator.validate("trAnsFeR 23456789 55555555 500");
		assertTrue(actual);
	}

	@Test
	public void first_character_in_transfer_command_is_upper() {
		boolean actual = commandValidator.validate("Transfer 23456789 55555555 500");
		assertTrue(actual);
	}

	@Test
	public void all_characters_in_transfer_command_are_lower() {
		boolean actual = commandValidator.validate("transfer 23456789 55555555 500");
		assertTrue(actual);
	}

	@Test
	public void all_characters_in_pass_command_are_upper() {
		boolean actual = commandValidator.validate("PASS 1");
		assertTrue(actual);
	}

	@Test
	public void all_characters_in_pass_command_are_lower() {
		boolean actual = commandValidator.validate("pass 1");
		assertTrue(actual);

	}

	@Test
	public void characters_in_pass_command_are_upper_and_lower() {
		boolean actual = commandValidator.validate("pASs 11");
		assertTrue(actual);
	}

	@Test
	public void incorrect_spelling_of_pass_command_type() {
		boolean actual = commandValidator.validate("peass 11");
		assertFalse(actual);
	}

	@Test
	public void alphanumeric_spelling_of_pass_command_type() {
		boolean actual = commandValidator.validate("p4ss 11");
		assertFalse(actual);
	}

}
