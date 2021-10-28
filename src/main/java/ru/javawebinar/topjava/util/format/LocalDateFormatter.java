package ru.javawebinar.topjava.util.format;

import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class LocalDateFormatter implements Formatter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    @NonNull
    public LocalDate parse(@NonNull String s, @NonNull Locale locale) throws ParseException {
        return Objects.requireNonNullElse(
                LocalDate.parse(s, FORMATTER.withLocale(locale)),
                DateTimeUtil.MIN_DATE.toLocalDate());
    }

    @Override
    @NonNull
    public String print(@NonNull LocalDate localDate, @NonNull Locale locale) {
        return localDate.format(FORMATTER.withLocale(locale));
    }
}
