package prompto.value;

import java.util.List;

import prompto.intrinsic.IterableWithCounts;
import prompto.runtime.Context;
import prompto.store.IStorable;

public interface IIterable<T extends IValue> extends IValue
{
    IterableWithCounts<T> getIterable(Context context);
    default void collectStorables(List<IStorable> storables) {
    	throw new UnsupportedOperationException();
    }
}

