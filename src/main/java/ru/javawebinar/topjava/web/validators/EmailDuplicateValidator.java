package ru.javawebinar.topjava.web.validators;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@Component
public class EmailDuplicateValidator implements Validator {
    private final UserService userService;

    private final MessageSource messageSource;

    private static final String EMAIL = "email";

    public EmailDuplicateValidator(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return User.class.isAssignableFrom(aClass) || UserTo.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {
        HasId user = (HasId) o;
        String email = o instanceof User ? ((User) o).getEmail() : ((UserTo) o).getEmail();
        User founded;
        try {
            founded = email == null ? null : userService.getByEmail(email);
        } catch (NotFoundException e) {
            return;
        }
        if (founded != null && (user.isNew() || user.id() != founded.id())) {
            errors.rejectValue(EMAIL, "email.duplicate", null, messageSource.getMessage("email.duplicate", null, LocaleContextHolder.getLocale()));
        }
    }
}
