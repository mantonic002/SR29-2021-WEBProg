package com.sr29_2021.Repository;

import com.sr29_2021.Model.Manufacturer;
import com.sr29_2021.Model.Vax;
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
public class VaxRepository implements IVaxRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IManufacturerRepository manRepo;

    private class VaxRowCallbackHandler implements RowCallbackHandler {

        private Map<Integer, Vax> Vaxes = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            String name = resultSet.getString(index++);
            Integer availableNum = resultSet.getInt(index++);
            Integer manufacturerId = resultSet.getInt(index++);

            Manufacturer manufacturer = manRepo.findOne(manufacturerId);

            Vax vax = Vaxes.get(id);
            if (vax == null) {
                vax = new Vax(id, name, availableNum, manufacturer);
                Vaxes.put(vax.getId(), vax);
            }
        }

        public List<Vax> getVaxes() { return new ArrayList<>(Vaxes.values()); }
    }

    @Override
    public Vax findOne(Integer id) {
        String sql =
                "SELECT v.id, v.name, v.available_num, v.manufacturer_id " +
                        "FROM vax v " +
                        "WHERE v.id = ? " +
                        "ORDER BY v.id";

        VaxRowCallbackHandler rowCallbackHandler = new VaxRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if(rowCallbackHandler.getVaxes().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getVaxes().get(0);
    }

    @Override
    public List<Vax> findByManufacturer(Manufacturer manufacturer) {
        String sql =
                "SELECT v.id, v.name, v.available_num, v.manufacturer_id " +
                        "FROM vax v " +
                        "WHERE v.manufacturer_id = ? " +
                        "ORDER BY v.id";

        VaxRowCallbackHandler rowCallbackHandler = new VaxRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, manufacturer.getId());

        if(rowCallbackHandler.getVaxes().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getVaxes();
    }

    @Override
    public List<Vax> findAll() {
        String sql =
                "SELECT v.id, v.name, v.available_num, v.manufacturer_id " +
                        "FROM vax v " +
                        "ORDER BY v.id";

        VaxRowCallbackHandler rowCallbackHandler = new VaxRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if(rowCallbackHandler.getVaxes().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getVaxes();
    }

    @Transactional
    @Override
    public int save(Vax vax) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO vax (name, available_num, manufacturer_id) values (?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, vax.getName());
                preparedStatement.setInt(index++, vax.getAvailableNum());
                preparedStatement.setInt(index++, vax.getManufacturer().getId());

                return preparedStatement;
            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return  success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Vax vax) {
        String sql = "UPDATE vax SET name = ?, available_num = ?, manufacturer_id = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                vax.getName(),
                vax.getAvailableNum(),
                vax.getManufacturer().getId(),
                vax.getId()) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM vax WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
