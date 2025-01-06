package banking;

public class DepositCommandValidator extends CommandValidator {

	public static final String SAVINGS = "savings";
	public static final String CHECKINGS = "checking";
	public static final int MAX_SAVINGS_DEPOSIT = 2500;
	public static final int MAX_CHECKING_DEPOSIT = 1000;

	public DepositCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		if (!isValidStructure(parts)) {
			return false;
		}

		String id = parts[1].toLowerCase();
		double amount = Double.parseDouble(parts[2]);

		if (!bank.accountExistsById(id)) {
			return false;
		}

		String accountType = bank.retrieveAccount(id).getType();
		return validateDepositRules(accountType, amount);
	}

	private boolean validateDepositRules(String accountType, double amount) {
		if (amount < 0) {
			return false;
		}

		switch (accountType) {
		case SAVINGS:
			return amount <= MAX_SAVINGS_DEPOSIT;
		case CHECKINGS:
			return amount <= MAX_CHECKING_DEPOSIT;
		default:
			return false;
		}

	}

	private boolean isValidStructure(String[] parts) {
		if (parts.length != 3) {
			return false;
		}
		try {
			Double.parseDouble(parts[2]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
