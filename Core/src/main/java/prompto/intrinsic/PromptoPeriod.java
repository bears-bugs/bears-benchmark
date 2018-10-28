package prompto.intrinsic;

import org.joda.time.Period;

import prompto.value.IMultiplyable;

/* Period is final so can't just extend it */
public class PromptoPeriod implements IMultiplyable {

	public static PromptoPeriod parse(String text) {
		return new PromptoPeriod(Period.parse(text));
	}
	
	Period wrapped;
	
	public PromptoPeriod(long duration) {
		wrapped = new Period(duration);
	}
	public PromptoPeriod(Period wrapped) {
		this.wrapped = wrapped;
	}

	public PromptoPeriod(long years, long months, long weeks, long days, long hours, long minutes, long seconds, long millis) {
		wrapped = new Period((int)years, (int)months, (int)weeks, (int)days, (int)hours, (int)minutes, (int)seconds, (int)millis);
	}

	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof PromptoPeriod && wrapped.equals(((PromptoPeriod)obj).wrapped);
	}
	
	@Override
	public String toString() {
		return wrapped.toString();
	}
	
	public PromptoPeriod plus(PromptoPeriod value) {
		return new PromptoPeriod(wrapped.plus(value.wrapped));
	}

	public PromptoPeriod minus(PromptoPeriod value) {
		return new PromptoPeriod(wrapped.minus(value.wrapped));
	}

	public long getNativeYears() {
		return wrapped.getYears();
	}

	public long getNativeMonths() {
		return wrapped.getMonths();
	}

	public long getNativeWeeks() {
		return wrapped.getWeeks();
	}

	public long getNativeDays() {
		return wrapped.getDays();
	}

	public long getNativeHours() {
		return wrapped.getHours();
	}

	public long getNativeMinutes() {
		return wrapped.getMinutes();
	}

	public long getNativeSeconds() {
		return wrapped.getSeconds();
	}

	public long getNativeMillis() {
		return wrapped.getMillis();
	}

    public PromptoPeriod multiply(int count) {
        return new PromptoPeriod(
              getNativeYears() * count,
              getNativeMonths() * count,
              getNativeWeeks() * count,
              getNativeDays() * count,
              getNativeHours() * count,
              getNativeMinutes() * count,
              getNativeSeconds() * count,
              getNativeMillis() * count);
    }
    
    
	public PromptoPeriod negate() {
		return new PromptoPeriod(-getNativeYears(),-getNativeMonths(),-getNativeWeeks(),
					-getNativeDays(),-getNativeHours(), -getNativeMinutes(),
					-getNativeSeconds(),-getNativeMillis());
	}

	
}
