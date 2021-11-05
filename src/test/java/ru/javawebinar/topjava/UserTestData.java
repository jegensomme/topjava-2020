package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Collections;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "meals");
    public static final TestMatcher<User> USER_MATCHER_WITH_MEALS = new TestMatcher<>(User.class,
            (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("registered", "meals.user").isEqualTo(e),
            (a, e) -> {
                throw new UnsupportedOperationException();
            }
    );


    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static User getDisabled() {
        User disabled = new User(user);
        disabled.setEnabled(false);
        return disabled;
    }

    public static User getWithMeals() {
        User withMeals = new User(user);
        withMeals.setMeals(MealTestData.meals);
        return withMeals;
    }
}
