package ru.javawebinar.topjava;

import org.assertj.core.matcher.AssertionMatcher;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TestUtil {
    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static <T> AssertionMatcher<T> getMatcher(TestMatcher<T> matcher, T expected) {
        return new AssertionMatcher<>() {
            @Override
            public void assertion(T actual) throws AssertionError {
                matcher.assertMatch(actual, expected);
            }
        };
    }

    @SafeVarargs
    public static <T> AssertionMatcher<Iterable<T>> getMatcher(TestMatcher<T> matcher, T... expected) {
        return getMatcher(matcher, List.of(expected));
    }

    public static <T> AssertionMatcher<Iterable<T>> getMatcher(TestMatcher<T> matcher, Iterable<T> expected) {
        return new AssertionMatcher<>() {
            @Override
            public void assertion(Iterable<T> actual) throws AssertionError {
                matcher.assertMatch(actual, expected);
            }
        };
    }
}
