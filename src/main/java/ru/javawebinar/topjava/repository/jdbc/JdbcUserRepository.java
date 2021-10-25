package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final RowMapper<User> ROW_MAPPER = ((resultSet, i) -> new User(
            resultSet.getInt("id"),
            resultSet.getString("name"), resultSet.getString("email"),
            resultSet.getString("password"), resultSet.getInt("calories_per_day"),
            resultSet.getBoolean("enabled"), resultSet.getDate("registered"),
            Collections.singleton(Role.valueOf(resultSet.getString("role"))))
    );

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            int updated = namedParameterJdbcTemplate.update("""
                UPDATE users SET name=:name, email=:email, password=:password,
                registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource);
            if (updated == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
        }
        Set<Role> roles = user.getRoles();
        jdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES(?, ?)",
                roles, roles.size(), (ps, r) -> {
                    ps.setInt(1, user.id());
                    ps.setString(2, r.name());
        });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("""
            SELECT u.*, ur.role FROM users u
            LEFT JOIN user_roles ur on u.id = ur.user_id
            WHERE u.id=?
            """, ROW_MAPPER, id);
        return singleResult(reduceResultSet(users));
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("""
            SELECT u.*, ur.role FROM users u
            LEFT JOIN user_roles ur on u.id = ur.user_id
            WHERE u.email=?
            """, ROW_MAPPER, email);
        return singleResult(reduceResultSet(users));
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("""
            SELECT u.*, ur.role FROM users u
            LEFT JOIN user_roles ur on u.id = ur.user_id
            ORDER BY name, email
            """, ROW_MAPPER);
        return reduceResultSet(users);
    }

    private static List<User> reduceResultSet(List<User> users) {
        return users.stream().collect(groupingBy(identity(), mapping(User::getRole, Collectors.toSet())))
                .entrySet().stream().map(e -> {
                    User user = e.getKey();
                    user.setRoles(e.getValue());
                    return user;
                }).collect(Collectors.toList());
    }
}
