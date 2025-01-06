package banking;

public class CommandValidator {
	protected Bank bank;

	public CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validate(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		if (parts.length == 0) {
			return false;
		}

		String commandType = parts[0].toLowerCase();

		switch (commandType) {
		case "create":
			return new CreateCommandValidator(bank).validate(givenCommand);
		case "deposit":
			return new DepositCommandValidator(bank).validate(givenCommand);
		case "withdraw":
			return new WithdrawCommandValidator(bank).validate(givenCommand);
		case "transfer":
			return new TransferCommandValidator(bank).validate(givenCommand);
		case "pass":
			return new PassTimeCommandValidator(bank).validate(givenCommand);
		default:
			return false;
		}
	}
}
