package prompto.store.memory;

import java.util.Map;

public class NotPredicate implements IPredicate {

	IPredicate predicate;
	
	public NotPredicate(IPredicate predicate) {
		this.predicate = predicate;
	}
	
	@Override
	public boolean matches(Map<String, Object> document) {
		return !predicate.matches(document);
	}

}
