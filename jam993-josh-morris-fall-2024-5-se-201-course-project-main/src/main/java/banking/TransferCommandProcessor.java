package banking;

public class TransferCommandProcessor extends CommandProcessor {

	public TransferCommandProcessor(Bank bank) {
		super(bank);
	}

	@Override
	public void process(String givenCommand) {
		String[] parts = givenCommand.split(" ");
		String id_source = parts[1];
		String id_destination = parts[2];
		double amount = Double.parseDouble(parts[3]);

		bank.transferDollars(id_source, id_destination, amount);
	}
}
