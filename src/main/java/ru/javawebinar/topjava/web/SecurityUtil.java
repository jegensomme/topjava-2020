package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int id = AbstractBaseEntity.START_SEQ;

    private static int caloriesPerDay = DEFAULT_CALORIES_PER_DAY;

    private SecurityUtil() {
    }

    public static int authUserId() {
        return id;
    }

    public static int authUserCaloriesPerDay() {
        return caloriesPerDay;
    }

    public static void setAuthUser(User user) {
        id = user.id();
        caloriesPerDay = user.getCaloriesPerDay();
    }
}