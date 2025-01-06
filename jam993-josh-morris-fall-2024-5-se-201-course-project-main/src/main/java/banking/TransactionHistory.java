package banking;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
	Bank bank;
	private List<String> output;

	public TransactionHistory(Bank bank) {
		this.bank = bank;
		this.output = new ArrayList<>();
	}

	public String getAccountState(String id) {
		Account account = bank.retrieveAccount(id);
		String accountType = account.getType();
		double apr = account.getAprValue();
		double balance = account.getBalance();
		String accountTypeFormatted = Character.toUpperCase(accountType.charAt(0)) + accountType.substring(1);
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);
		String balanceFormatted = decimalFormat.format(balance);
		String aprFormatted = decimalFormat.format(apr);
		return String.format("%s %s %s %s", accountTypeFormatted, id, balanceFormatted, aprFormatted);
	}

	public List<String> getTransactionHistory(List<String> validCommands, List<String> invalidCommands) {
		for (String id : bank.getOpenAccounts()) {
			if (bank.accountExistsById(id)) {
				output.add(getAccountState(id));
				addValidCommands(output, validCommands, id);
			}
		}
		output.addAll(invalidCommands);
		return new ArrayList<>(output);
	}

	private void addValidCommands(List<String> output, List<String> validCommands, String id) {
		for (String command : validCommands) {
			String[] parts = command.split(" ");
			String commandType = parts[0].toLowerCase();

			if (isTransactionalCommand(commandType, parts, id)) {
				String formattedCommand = formatCommand(command);
				output.add(formattedCommand);
			}
		}

	}

	private String formatCommand(String command) {
		return command.substring(0, 1).toUpperCase() + command.substring(1).toLowerCase();
	}

	private boolean isTransactionalCommand(String commandType, String[] parts, String id) {
		return ((!commandType.equals("create") && !commandType.equals("pass"))
				&& (parts[1].equals(id) || parts[2].equals(id)));
	}
}
