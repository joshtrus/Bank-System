package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandStorageTest {
	private CommandStorage commandStorage;

	@BeforeEach
	public void setUp() {
		commandStorage = new CommandStorage();
	}

	@Test
	public void invalid_list_is_initially_empty() {
		List<String> commands = commandStorage.getInvalidCommands();
		assertTrue(commands.isEmpty());
	}

	@Test
	public void valid_list_is_initially_empty() {
		List<String> commands = commandStorage.getValidCommands();
		assertTrue(commands.isEmpty());
	}

	@Test
	public void invalid_command_can_be_added_to_list() {
		commandStorage.addInvalidCommand("Create 12345678");
		List<String> commands = commandStorage.getInvalidCommands();

		assertEquals(1, commands.size());
		assertEquals("Create 12345678", commands.get(0));
	}

	@Test
	public void valid_command_can_be_added_to_list() {
		commandStorage.addValidCommand("Create savings 23456789 0.6");
		List<String> commands = commandStorage.getValidCommands();

		assertEquals(1, commands.size());
		assertEquals("Create savings 23456789 0.6", commands.get(0));
	}

	@Test
	public void multiple_invalid_commands_can_be_added_to_list_at_once() {
		commandStorage.addInvalidCommand("Create 12345678");
		commandStorage.addInvalidCommand("Create savings");
		commandStorage.addInvalidCommand("Create cd");
		List<String> commands = commandStorage.getInvalidCommands();

		assertEquals(3, commands.size());
		assertEquals("Create 12345678", commands.get(0));
		assertEquals("Create savings", commands.get(1));
		assertEquals("Create cd", commands.get(2));
	}

	@Test
	public void multiple_valid_commands_can_be_added_to_list_at_once() {
		commandStorage.addValidCommand("Create savings 23456789 0.6");
		commandStorage.addValidCommand("Create savings 11111111 0.6");
		commandStorage.addValidCommand("Create savings 22222222 0.6");
		List<String> commands = commandStorage.getValidCommands();

		assertEquals(3, commands.size());
		assertEquals("Create savings 23456789 0.6", commands.get(0));
		assertEquals("Create savings 11111111 0.6", commands.get(1));
		assertEquals("Create savings 22222222 0.6", commands.get(2));
	}

	@Test
	public void get_all_invalid_commands_in_list() {
		commandStorage.addInvalidCommand("Create 12345678");
		commandStorage.addInvalidCommand("Create savings");
		List<String> commands = commandStorage.getInvalidCommands();

		assertEquals(List.of("Create 12345678", "Create savings"), commands);
	}

	@Test
	public void get_all_valid_commands_in_list() {
		commandStorage.addValidCommand("Create savings 22222222 0.6");
		commandStorage.addValidCommand("Create savings 11111111 0.6");
		List<String> commands = commandStorage.getValidCommands();

		assertEquals(List.of("Create savings 22222222 0.6", "Create savings 11111111 0.6"), commands);
	}
}
