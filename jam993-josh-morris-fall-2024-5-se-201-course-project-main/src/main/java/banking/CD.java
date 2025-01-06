package banking;

public class CD extends Account {
	private String type = "cd";

	public CD(double apr, double dollars, String id) {
		super(apr, dollars, id);
	}

	@Override
	public String getType() {
		return type;
	}
}
