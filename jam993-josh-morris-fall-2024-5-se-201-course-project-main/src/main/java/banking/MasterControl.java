package banking;

import java.util.List;

public class MasterControl {
	private CommandValidator commandValidator;
	private CommandProcessor commandProcessor;
	private CommandStorage commandStorage;
	private TransactionHistory transactionHistory;

	public MasterControl(CommandValidator commandValidator, CommandProcessor commandProcessor,
			CommandStorage commandStorage, TransactionHistory transactionHistory) {
		this.commandValidator = commandValidator;
		this.commandProcessor = commandProcessor;
		this.commandStorage = commandStorage;
		this.transactionHistory = transactionHistory;
	}

	public List<String> start(List<String> input) {
		for (String command : input) {
			if (commandValidator.validate(command)) {
				commandProcessor.process(command);
				commandStorage.addValidCommand(command);

			} else {
				commandStorage.addInvalidCommand(command);
			}
		}
		return transactionHistory.getTransactionHistory(commandStorage.getValidCommands(),
				commandStorage.getInvalidCommands());
	}

}
