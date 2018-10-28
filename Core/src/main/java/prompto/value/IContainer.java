package prompto.value;

import prompto.error.PromptoError;
import prompto.runtime.Context;

public interface IContainer<T extends IValue> extends IIterable<T>
{
    long getLength();
    default boolean isEmpty() { return getLength()==0; }
    boolean hasItem(Context context, IValue iValue) throws PromptoError;
    T getItem(Context context, IValue item) throws PromptoError;
}

