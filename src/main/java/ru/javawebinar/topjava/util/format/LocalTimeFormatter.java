package ru.javawebinar.topjava.util.format;

import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    @NonNull
    public LocalTime parse(@NonNull String s, @NonNull Locale locale) throws ParseException {
        return Objects.requireNonNullElse(
                LocalTime.parse(s, FORMATTER.withLocale(locale)),
                LocalTime.MIN);
    }

    @Override
    @NonNull
    public String print(@NonNull LocalTime localTime, @NonNull Locale locale) {
        return localTime.format(FORMATTER.withLocale(locale));
    }
}
