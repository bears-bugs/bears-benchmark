package valuestreams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.function.Function;

public class DateValue extends AbstractValue<Date> {
    private DateValue(Date value) {
        super(value);
    }

    private DateValue() {
    }

    public static DateValue of(Date value) {
        return new DateValue(value);
    }

    public static DateValue of(int day, int month, int year) {
        return new DateValue(new GregorianCalendar(year, month, day).getTime());
    }

    public static DateValue of(int day, Month month, int year) {
        return new DateValue(new GregorianCalendar(year, month.getValue(), day).getTime());
    }

    public static DateValue of(String dateString, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            return new DateValue(formatter.parse(dateString));
        } catch (ParseException e) {
            return DateValue.empty();
        }
    }

    public static DateValue empty() {
        return new DateValue();
    }

    private static LocalDate getLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public DateValue validate(Function<Date, Boolean> validator) {
        if (isPresent()) {
            if (!validator.apply(value)) {
                value = null;
            }
        }

        return this;
    }

    public DateValue map(Function<Date, Date> mapper) {
        Objects.requireNonNull(mapper);
        return isEmpty() ? DateValue.empty() : DateValue.of(mapper.apply(this.value));
    }

    public <T> Value<T> mapCast(Function<Date, T> mapper) {
        Objects.requireNonNull(mapper);
        return isEmpty() ? Value.empty() : Value.of(mapper.apply(this.value));
    }

    public DateValue isBefore(Date target) {
        return validate(d -> d.before(target));
    }

    public DateValue isAfter(Date target) {
        return validate(d -> d.after(target));
    }

    public DateValue inYear(int year) {
        return validate(d -> getLocalDate(d).getYear() == year);
    }

    public DateValue inMonth(Month month) {
        return validate(d -> getLocalDate(d).getMonth() == month);
    }

    public DateValue onDay(int monthDay) {
        return validate(d -> getLocalDate(d).getDayOfMonth() == monthDay);
    }

    public DateValue past() {
        return isBefore(new Date());
    }

    public DateValue future() {
        return isAfter(new Date());
    }
}
