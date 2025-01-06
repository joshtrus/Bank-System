package banking;

public class CommandProcessor {
	protected Bank bank;

	public CommandProcessor(Bank bank) {
		this.bank = bank;
	}

	public void process(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		String commandType = parts[0].toLowerCase();

		if (commandType.equals("create")) {
			new CreateCommandProcessor(bank).process(givenCommand);
		} else if (commandType.equals("deposit")) {
			new DepositCommandProcessor(bank).process(givenCommand);
		} else if (commandType.equals("withdraw")) {
			new WithdrawCommandProcessor(bank).process(givenCommand);
		} else if (commandType.equals("transfer")) {
			new TransferCommandProcessor(bank).process(givenCommand);
		} else if (commandType.equals("pass")) {
			new PassTimeCommandProcessor(bank).process(givenCommand);
		}

	}
}
