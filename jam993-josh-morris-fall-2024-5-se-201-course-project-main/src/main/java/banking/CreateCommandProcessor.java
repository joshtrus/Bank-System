package banking;

public class CreateCommandProcessor extends CommandProcessor {

	public CreateCommandProcessor(Bank bank) {
		super(bank);
	}

	@Override
	public void process(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		String accountType = parts[1].toLowerCase();
		String id = parts[2];
		double apr = Double.parseDouble(parts[3]);

		if (parts.length < 5) {
			bank.addAccount(apr, 0, id, accountType);

		} else {
			double balance = Double.parseDouble(parts[4]);
			bank.addAccount(apr, balance, id, accountType);
		}

	}

}
