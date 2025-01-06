package banking;

public class PassTimeCommandValidator extends CommandValidator {
	public PassTimeCommandValidator(Bank bank) {
		super(bank);
	}

	@Override
	public boolean validate(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		if (parts.length != 2) {
			return false;
		}
		String month = parts[1];

		try {
			int Month = Integer.parseInt(month);
			if (Month < 1 || Month > 60) {
				return false;
			}

		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

}
