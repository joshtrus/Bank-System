package banking;

public abstract class Account {

	protected double dollars;

	protected double apr;

	protected String id;

	protected int time;

	protected Account(double apr, String id) {
		dollars = 0;
		this.apr = apr;
		this.id = id;
		this.time = 0;
	}

	protected Account(double apr, double dollars, String id) {
		this.apr = apr;
		this.dollars = dollars;
		this.id = id;
		this.time = 0;
	}

	public double getBalance() {
		return dollars;
	}

	public void addDollars(double dollarsToAdd) {
		dollars += dollarsToAdd;
	}

	public void minusDollars(double dollarsToMinus) {
		if (dollars > dollarsToMinus) {
			dollars -= dollarsToMinus;
		} else {
			dollars = 0;
		}
	}

	public double getAprValue() {
		return apr;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return "";
	}

	public void increasePassTime() {
		time++;
	}

	public int getPassTime() {
		return time;
	}
}
