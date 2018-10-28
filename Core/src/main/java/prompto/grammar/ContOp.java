package prompto.grammar;

public enum ContOp {
	IN,
	HAS,
	HAS_ALL,
	HAS_ANY,
	NOT_IN,
	NOT_HAS,
	NOT_HAS_ALL,
	NOT_HAS_ANY;

	public String toString() {
		return this.name().toLowerCase().replace('_', ' ');
	}

	public ContOp reverse() {
		switch(this) {
		case IN:
			return HAS;
		case HAS:
			return IN;
		case NOT_IN:
			return NOT_HAS;
		case NOT_HAS:
			return NOT_IN;
		default:
			return null;
		}
	}
}
