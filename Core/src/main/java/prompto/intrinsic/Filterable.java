package prompto.intrinsic;

import java.util.function.Predicate;

public interface Filterable<C,V> {
	C filter(Predicate<V> p);
}
