package banking;

public class WithdrawCommandProcessor extends CommandProcessor {
	public WithdrawCommandProcessor(Bank bank) {
		super(bank);
	}

	@Override
	public void process(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		String id = parts[1];
		double amount = Double.parseDouble(parts[2]);

		bank.minusDollars(amount, id);
	}
}