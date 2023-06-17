package com.sr29_2021.Repository;

import com.sr29_2021.Model.Manufacturer;
import com.sr29_2021.Repository.Interfaces.IManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ManufacturerRepository implements IManufacturerRepository {
    private final JdbcTemplate jdbcTemplate;

    public ManufacturerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class ManufacturerRowCallBackHandler implements RowCallbackHandler {

        private final Map<Integer, Manufacturer> Manufacturers = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            String name = resultSet.getString(index++);
            String country = resultSet.getString(index++);


            Manufacturer manufacturer = Manufacturers.get(id);
            if (manufacturer == null) {
                manufacturer = new Manufacturer(id, name, country);
                Manufacturers.put(manufacturer.getId(), manufacturer);
            }
        }

        public List<Manufacturer> getManufacturers() {
            return new ArrayList<>(Manufacturers.values());
        }

    }

    @Override
    public Manufacturer findOne(Integer id) {
        String sql =
                "SELECT man.id, man.name, man.country " +
                        "FROM manufacturers man " +
                        "WHERE man.id = ? " +
                        "ORDER BY man.id";

        ManufacturerRowCallBackHandler rowCallbackHandler = new ManufacturerRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (rowCallbackHandler.getManufacturers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getManufacturers().get(0);
    }

    @Override
    public Manufacturer findOne(String name) {
        String sql =
                "SELECT man.id, man.name, man.country " +
                        "FROM manufacturers man " +
                        "WHERE man.name = ? " +
                        "ORDER BY man.id";

        ManufacturerRowCallBackHandler rowCallbackHandler = new ManufacturerRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, name);

        if (rowCallbackHandler.getManufacturers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getManufacturers().get(0);
    }

    @Override
    public List<Manufacturer> findAll() {
        String sql =
                "SELECT man.id, man.name, man.country " +
                        "FROM manufacturers man " +
                        "ORDER BY man.id";

        ManufacturerRowCallBackHandler rowCallbackHandler = new ManufacturerRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getManufacturers().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getManufacturers();
    }

    @Transactional
    @Override
    public int save(Manufacturer manufacturer) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO manufacturers (name, country) VALUES (?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, manufacturer.getName());
                preparedStatement.setString(index++, manufacturer.getCountry());

                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Manufacturer manufacturer) {
        String sql = "UPDATE manufacturers SET name = ?, country = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                manufacturer.getName(),
                manufacturer.getCountry(),
                manufacturer.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM manufacturers WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
