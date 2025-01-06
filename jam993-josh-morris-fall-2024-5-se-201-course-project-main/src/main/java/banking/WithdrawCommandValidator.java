package banking;

public class WithdrawCommandValidator extends CommandValidator {
	private static final String SAVINGS = "savings";
	private static final String CHECKINGS = "checking";
	private static final String CD = "cd";
	private static final double SAVINGS_MAX_WITHDRAW = 1000;
	private static final double CHECKINGS_MAX_WITHDRAW = 400;

	public WithdrawCommandValidator(Bank bank) {
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
		return validateWithdrawalRules(accountType, id, amount);
	}

	private boolean validateWithdrawalRules(String accountType, String id, double amount) {
		if (amount < 0) {
			return false;
		}

		switch (accountType) {
		case SAVINGS:
			return amount <= SAVINGS_MAX_WITHDRAW && bank.getWithdrawal() == 0;

		case CHECKINGS:
			return amount <= CHECKINGS_MAX_WITHDRAW;

		case CD:
			double balance = bank.retrieveAccount(id).getBalance();
			int passTime = bank.retrieveAccount(id).getPassTime();
			return amount >= balance && passTime >= 12;

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
