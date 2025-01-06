package banking;

import java.util.Objects;

public class CreateCommandValidator extends CommandValidator {

	public static final String CD = "cd";
	public static final String SAVINGS = "savings";
	public static final String CHECKINGS = "checking";

	CreateCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String givenCommand) {
		String[] parts = givenCommand.split(" ");

		if (!isValidStructure(parts)) {
			return false;
		}

		String accountType = parts[1].toLowerCase();
		String id = parts[2].toLowerCase();
		double apr = Double.parseDouble(parts[3]);

		return validateAccountCreation(accountType, id, apr, parts);
	}

	private boolean validateAccountCreation(String accountType, String id, double apr, String[] parts) {
		if (!validateCommand(accountType, id, apr)) {
			return false;
		}

		if (accountType.equals(CD)) {
			return validateCdAccount(parts);
		}

		return (accountType.equals(SAVINGS) || accountType.equals(CHECKINGS)) && parts.length == 4;
	}

	private boolean validateCommand(String accountType, String id, double apr) {
		if (!validateAccountType(accountType)) {
			return false;
		}

		if (!validateAccountId(id)) {
			return false;
		}

		return validateAPR(apr);
	}

	private boolean validateCdAccount(String[] parts) {
		if (parts.length != 5) {
			return false;
		}
		try {
			int balance = Integer.parseInt(parts[4]);
			return balance >= 1000 && balance <= 10000;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean validateAPR(double apr) {
		return apr >= 0 && apr <= 10;
	}

	private boolean validateAccountId(String id) {
		try {
			Integer.parseInt(id);
			return id.length() == 8 && !bank.accountExistsById(id);
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean validateAccountType(String accountType) {
		return Objects.equals(accountType, CD) || Objects.equals(accountType, SAVINGS)
				|| Objects.equals(accountType, CHECKINGS);
	}

	private boolean isValidStructure(String[] parts) {
		if (parts.length < 4) {
			return false;
		}
		try {
			Double.parseDouble(parts[3]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
