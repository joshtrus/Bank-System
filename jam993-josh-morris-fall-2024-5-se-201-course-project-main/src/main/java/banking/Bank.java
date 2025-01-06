package banking;

import java.util.*;

public class Bank {
	private Map<String, Account> accounts;
	private List<String> openAccounts;

	private int withdrawCounter;

	Bank() {
		accounts = new HashMap<>();
		withdrawCounter = 0;
		openAccounts = new ArrayList<>();
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public void addAccount(double apr, double dollars, String id, String name) {
		if (Objects.equals(name, "savings")) {
			accounts.put(id, new Savings(apr, id));
			openAccounts.add(id);
		} else if (Objects.equals(name, "checking")) {
			accounts.put(id, new Checking(apr, id));
			openAccounts.add(id);
		} else if (Objects.equals(name, "cd")) {
			accounts.put(id, new CD(apr, dollars, id));
			openAccounts.add(id);
		}
	}

	public Account retrieveAccount(String id) {
		return accounts.get(id);
	}

	public void addDollars(double dollarsToAdd, String id) {
		retrieveAccount(id).addDollars(dollarsToAdd);
	}

	public void minusDollars(double dollarsToMinus, String id) {
		setWithdraw();
		retrieveAccount(id).minusDollars(dollarsToMinus);
	}

	public void transferDollars(String id_source, String id_destination, double amount) {
		double source_balance = retrieveAccount(id_source).getBalance();

		addDollars(Math.min(source_balance, amount), id_destination);
		minusDollars(amount, id_source);

	}

	public void passTime(int months) {
		resetWithdrawals();
		List<String> accountsToRemove = new ArrayList<>();

		while (months > 0) {
			for (Map.Entry<String, Account> entry : accounts.entrySet()) {
				Account account = entry.getValue();
				applyTimeToAccount(account, accountsToRemove);
			}

			closeAccounts(accountsToRemove);
			months--;
		}
	}

	private void applyTimeToAccount(Account account, List<String> accountsToRemove) {
		account.increasePassTime();
		double balance = account.getBalance();

		if (balance == 0) {
			accountsToRemove.add(account.getId());
		} else if (balance < 100) {
			account.minusDollars(25);
		}

		applyApr(account);
	}

	private void applyApr(Account account) {
		String accountType = account.getType();
		double apr = account.getAprValue();
		double balance = account.getBalance();

		if (accountType.equals("savings") || accountType.equals("checking")) {
			account.addDollars(calculateAprValue(apr, balance));
		} else if (accountType.equals("cd")) {
			for (int j = 0; j < 4; j++) {
				account.addDollars(calculateAprValue(apr, balance));
			}
		}
	}

	private void closeAccounts(List<String> accountsToRemove) {
		for (String id : accountsToRemove) {
			accounts.remove(id);
			openAccounts.remove(id);

		}

	}

	public double calculateAprValue(double apr, double balance) {
		return (apr / 100 / 12) * balance;
	}

	public void setWithdraw() {
		withdrawCounter += 1;
	}

	public void resetWithdrawals() {
		withdrawCounter = 0;
	}

	public int getWithdrawal() {
		return withdrawCounter;
	}

	public List<String> getOpenAccounts() {
		return openAccounts;
	}

	public boolean accountExistsById(String id) {
		return accounts.get(id) != null;
	}
}