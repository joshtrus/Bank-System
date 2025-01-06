package banking;

public class Savings extends Account {
	private String type = "savings";

	public Savings(double apr, String id) {
		super(apr, id);
	}

	@Override
	public String getType() {
		return type;
	}

}
