package prompto.value;

public interface INumber extends IValue, Comparable<INumber>
{
    long longValue();
    double doubleValue();
}

