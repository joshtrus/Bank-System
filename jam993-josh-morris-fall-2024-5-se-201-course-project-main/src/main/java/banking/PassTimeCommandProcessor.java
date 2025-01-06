package banking;

public class PassTimeCommandProcessor extends CommandProcessor {
	public PassTimeCommandProcessor(Bank bank) {
		super(bank);
	}

	@Override
	public void process(String givenCommmand) {
		String[] parts = givenCommmand.split(" ");
		int months = Integer.parseInt(parts[1]);
		bank.passTime(months);
	}
}
