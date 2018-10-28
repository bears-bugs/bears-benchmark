package prompto.value;

import prompto.intrinsic.Filterable;
import prompto.runtime.Context;

public interface IFilterable extends IValue
{
    Filterable<IValue,IValue> getFilterable(Context context);
}

