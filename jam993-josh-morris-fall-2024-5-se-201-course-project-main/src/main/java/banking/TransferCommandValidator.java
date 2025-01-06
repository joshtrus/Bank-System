package banking;

public class TransferCommandValidator extends CommandValidator {
	private static final String SAVINGS = "savings";
	private static final String CHECKINGS = "checking";
	private static final double SAVINGS_MAX_TRANSFER = 1000;
	private static final double CHECKINGS_MAX_TRANSFER = 400;

	public TransferCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		if (!isValidStructure(parts)) {
			return false;
		}

		String id1 = parts[1].toLowerCase();
		String id2 = parts[2].toLowerCase();
		double amount = Double.parseDouble(parts[3]);

		if (!areAccountsValid(id1, id2)) {
			return false;
		}

		String accountType1 = bank.retrieveAccount(id1).getType();
		String accountType2 = bank.retrieveAccount(id2).getType();

		return validateTransferRules(accountType1, accountType2, amount);
	}

	private boolean isValidStructure(String[] parts) {
		if (parts.length != 4) {
			return false;
		}
		try {
			Double.parseDouble(parts[3]);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean areAccountsValid(String id1, String id2) {
		if (id1.equals(id2)) {
			return false;
		}
		return bank.accountExistsById(id1) && bank.accountExistsById(id2);
	}

	private boolean validateTransferRules(String accountType1, String accountType2, double amount) {
		if (amount < 0) {
			return false;
		}

		if (sourceIsSavings(accountType1, accountType2)) {
			return validateSavingsTransfer(amount);
		}

		if (sourceIsChecking(accountType1, accountType2)) {
			return validateCheckingTransfer(amount);
		}

		return false;
	}

	private boolean sourceIsChecking(String accountType1, String accountType2) {
		return accountType1.equals(CHECKINGS) && (accountType2.equals(SAVINGS) || accountType2.equals(CHECKINGS));
	}

	private boolean sourceIsSavings(String accountType1, String accountType2) {
		return accountType1.equals(SAVINGS) && (accountType2.equals(SAVINGS) || accountType2.equals(CHECKINGS));
	}

	private boolean validateSavingsTransfer(double amount) {
		return amount <= SAVINGS_MAX_TRANSFER && bank.getWithdrawal() == 0;
	}

	private boolean validateCheckingTransfer(double amount) {
		return amount <= CHECKINGS_MAX_TRANSFER;
	}

}
