package com.sr29_2021.Repository;

import com.sr29_2021.Model.User;
import com.sr29_2021.Model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository implements IUserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    private class UserRowCallBackHandler implements RowCallbackHandler {

        private Map<Integer, User> Users = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            String email = resultSet.getString(index++);
            String firstName = resultSet.getString(index++);
            String lastName = resultSet.getString(index++);
            String password = resultSet.getString(index++);
            String jmbg = resultSet.getString(index++);
            String address = resultSet.getString(index++);
            String phoneNum = resultSet.getString(index++);
            UserRole role = UserRole.valueOf(resultSet.getString(index++));
            LocalDateTime registrationTime = resultSet.getTimestamp(index++).toLocalDateTime();
            LocalDate date = resultSet.getDate(index++).toLocalDate();


            User user = Users.get(id);
            if (user == null) {
                user = new User(id, email, firstName, lastName, password, jmbg, address, phoneNum, role, registrationTime, date);
                Users.put(user.getId(), user); // dodavanje u kolekciju
            }
        }

        public List<User> getUsers() {
            return new ArrayList<>(Users.values());
        }

    }

    @Override
    public User findOne(Integer id) {
        String sql =
                "SELECT kor.id, kor.email, kor.first_name, kor.last_name, kor.password, kor.jmbg, kor.address, kor.phone_num, kor.user_role, kor.registration_date, kor.birth_date " +
                        "FROM users kor " +
                        "WHERE kor.id = ? " +
                        "ORDER BY kor.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers().get(0);
    }

    @Override
    public User findOne(String email) {
        String sql =
                "SELECT kor.id, kor.email, kor.first_name, kor.last_name, kor.password, kor.jmbg, kor.address, kor.phone_num, kor.user_role, kor.registration_date, kor.birth_date " +
                        "FROM users kor " +
                        "WHERE kor.email = ? " +
                        "ORDER BY kor.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email);

        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers().get(0);
    }

    @Override
    public User findOne(String email, String sifra) {
        String sql =
                "SELECT kor.id, kor.email, kor.first_name, kor.last_name, kor.password, kor.jmbg, kor.address, kor.phone_num, kor.user_role, kor.registration_date, kor.birth_date " +
                        "FROM users kor " +
                        "WHERE kor.email = ? AND " +
                        "kor.password = ? " +
                        "ORDER BY kor.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email, sifra);

        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers().get(0);
    }

    @Override
    public List<User> findAll() {
        String sql =
                "SELECT kor.id, kor.email, kor.first_name, kor.last_name, kor.password, kor.jmbg, kor.address, kor.phone_num, kor.user_role, kor.registration_date, kor.birth_date " +
                        "FROM users kor " +
                        "ORDER BY kor.id";

        UserRowCallBackHandler rowCallbackHandler = new UserRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getUsers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getUsers();
    }

    @Transactional
    @Override
    public int save(User user) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO users (email, first_name, last_name, password, jmbg, address, phone_num, user_role, registration_date, birth_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, user.getEmail());
                preparedStatement.setString(index++, user.getFirstName());
                preparedStatement.setString(index++, user.getLastName());
                preparedStatement.setString(index++, user.getPassword());
                preparedStatement.setString(index++, user.getJmbg());
                preparedStatement.setString(index++, user.getAddress());
                preparedStatement.setString(index++, user.getPhoneNum());
                preparedStatement.setString(index++, user.getRole().toString());

                Timestamp timestamp = Timestamp.valueOf(user.getRegistrationTime());
                preparedStatement.setString(index++, timestamp.toString());

                java.sql.Date date= Date.valueOf(user.getBirthDate());
                preparedStatement.setString(index++, date.toString());

                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(User user) {
        String sql = "UPDATE users SET email = ?, first_name = ?, last_name = ?, password = ?, jmbg = ?, address = ?, phone_num = ?, user_role = ?, registration_date = ?, birth_date = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getJmbg(),
                user.getAddress(),
                user.getPhoneNum(),
                user.getRole().toString(),
                Timestamp.valueOf(user.getRegistrationTime()).toString(),
                user.getBirthDate().toString(),
                user.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
