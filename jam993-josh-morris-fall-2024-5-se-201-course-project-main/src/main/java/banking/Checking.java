package banking;

public class Checking extends Account {

	private String type = "checking";

	public Checking(double apr, String id) {
		super(apr, id);
	}

	@Override
	public String getType() {
		return type;
	}

}
