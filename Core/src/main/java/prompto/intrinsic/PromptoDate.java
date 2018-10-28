package prompto.intrinsic;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

/* LocalDate is final so can't just extend it */
public class PromptoDate implements Comparable<PromptoDate> {

	public static PromptoDate parse(String text) {
		return new PromptoDate(LocalDate.parse(text));
	}
	
	public static PromptoDate fromJavaTime(long time) {
		return new PromptoDate(new LocalDate(time, DateTimeZone.UTC));
	}

	LocalDate wrapped;

	public PromptoDate(LocalDate wrapped) {
		this.wrapped = wrapped;
	}

	public PromptoDate(int year, int month, int day) {
		this.wrapped = new LocalDate(year, month, day);
	}
	
	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PromptoDate && wrapped.equals(((PromptoDate)obj).wrapped);
	}
	
	@Override
	public String toString() {
		return wrapped.toString();
	}
	
	@Override
	public int compareTo(PromptoDate o) {
		return wrapped.compareTo(o.wrapped);
	}

	public Long getYear() {
		return (long)wrapped.getYear();
	}

	public Long getMonth() {
		return (long)wrapped.getMonthOfYear();
	}

	public Long getDayOfMonth() {
		return (long)wrapped.getDayOfMonth();
	}

	public Long getDayOfYear() {
		return (long)wrapped.getDayOfYear();
	}
	
	public long getNativeYear() {
		return wrapped.getYear();
	}

	public long getNativeMonth() {
		return wrapped.getMonthOfYear();
	}

	public long getNativeDayOfMonth() {
		return wrapped.getDayOfMonth();
	}

	public long getNativeDayOfYear() {
		return wrapped.getDayOfYear();
	}

	public PromptoDate plusDays(long count) {
		return new PromptoDate(wrapped.plusDays((int)count));
	}

	public boolean isAfter(PromptoDate value) {
		return wrapped.isAfter(value.wrapped);
	}

	public PromptoDate plus(PromptoPeriod period) {
		return new PromptoDate(wrapped.plus(period.wrapped));
	}

	public PromptoDate minus(PromptoPeriod period) {
		return new PromptoDate(wrapped.minus(period.wrapped));
	}

	public PromptoPeriod minus(PromptoDate other) {
		return new PromptoPeriod(
				this.getNativeYear() - other.getNativeYear(),
				this.getNativeMonth() - other.getNativeMonth(), 
				0,
				this.getNativeDayOfMonth() - other.getNativeDayOfMonth(), 
				0, 0, 0, 0);
	}

	public long toJavaTime() {
		return wrapped.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis();
	}

	public String format(String formatString) {
		return wrapped.toString(formatString);
	}

	
}
