package ru.javawebinar.topjava.web.validators;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

@Component
public class MealDuplicateValidator implements Validator {
    private final MealService mealService;

    private final MessageSource messageSource;

    private static final String DATE_TIME = "dateTime";

    public MealDuplicateValidator(MealService mealService, MessageSource messageSource) {
        this.mealService = mealService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return Meal.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        Meal meal = (Meal) o;
        Meal founded;
        try {
            founded = meal.getDateTime() == null ? null : mealService.getByDateTime(meal.getDateTime(), SecurityUtil.authUserId());
        } catch (NotFoundException e) {
            return;
        }
        if (founded != null && (meal.isNew() || meal.id() != founded.id())) {
            errors.rejectValue(DATE_TIME, "meal.duplicate", null, messageSource.getMessage("meal.duplicate", null, LocaleContextHolder.getLocale()));
        }
    }
}
