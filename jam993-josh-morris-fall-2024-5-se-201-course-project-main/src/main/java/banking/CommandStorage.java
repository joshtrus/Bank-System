package banking;

import java.util.ArrayList;
import java.util.List;

public class CommandStorage {
	protected List<String> invalidCommands;
	protected List<String> validCommands;

	public CommandStorage() {
		invalidCommands = new ArrayList<>();
		validCommands = new ArrayList<>();
	}

	public void addInvalidCommand(String command) {
		invalidCommands.add(command);
	}

	public List<String> getInvalidCommands() {
		return new ArrayList<>(invalidCommands);
	}

	public void addValidCommand(String command) {
		validCommands.add(command);
	}

	public List<String> getValidCommands() {
		return new ArrayList<>(validCommands);
	}
}
