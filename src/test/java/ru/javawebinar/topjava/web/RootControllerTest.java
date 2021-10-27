package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.getMatcher;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", getMatcher(USER_MATCHER, admin, user)));
    }

    @Test
    void getMeals() throws Exception {
        perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals",
                        getMatcher(MEAL_TO_MATCHER, getTos(meals, user.getCaloriesPerDay()))));
    }
}