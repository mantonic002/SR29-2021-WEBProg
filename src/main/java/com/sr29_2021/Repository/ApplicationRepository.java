package com.sr29_2021.Repository;

import com.sr29_2021.Model.Application;
import com.sr29_2021.Model.Patient;
import com.sr29_2021.Model.Vax;
import com.sr29_2021.Repository.Interfaces.*;
import com.sr29_2021.Repository.Interfaces.IApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ApplicationRepository implements IApplicationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IPatientRepository patientRepo;
    @Autowired
    private IVaxRepository vaxRepo;

    private class ApplicationRowCallBackHandler implements RowCallbackHandler {

        private Map<Integer, Application> ApplicationMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            LocalDateTime dateTime = resultSet.getTimestamp(index++).toLocalDateTime();
            Integer patientId = resultSet.getInt(index++);
            Integer vaxId = resultSet.getInt(index++);

            Patient patient = patientRepo.findOne(patientId);
            Vax vax = vaxRepo.findOne(vaxId);

            Application application = ApplicationMap.get(id);
            if (application == null) {
                application = new Application(id, dateTime.minusHours(2), patient, vax);
                ApplicationMap.put(application.getId(), application); // dodavanje u kolekciju
            }
        }

        public List<Application> getApplications() {
            return new ArrayList<>(ApplicationMap.values());
        }

    }

    @Override
    public Application findOne(Integer id) {
        String sql =
                "SELECT n.id, n.date_time, n.patient_id, n.vax_id " +
                        "FROM applications n " +
                        "WHERE n.id = ? " +
                        "ORDER BY n.id";

        ApplicationRepository.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRepository.ApplicationRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (rowCallbackHandler.getApplications().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getApplications().get(0);
    }

    @Override
    public List<Application> findAll() {
        String sql =
                "SELECT n.id, n.date_time, n.patient_id, n.vax_id " +
                        "FROM applications n " +
                        "ORDER BY n.id";

        ApplicationRepository.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRepository.ApplicationRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getApplications().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getApplications();
    }

    @Transactional
    @Override
    public int save(Application application) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO applications (date_time, patient_id, vax_id) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                Timestamp timestamp = Timestamp.valueOf(application.getDateTime());
                preparedStatement.setString(index++, timestamp.toString());
                preparedStatement.setInt(index++, application.getPatientId());
                preparedStatement.setInt(index++, application.getVaxId());

                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Application application) {
        String sql = "UPDATE applications SET date_time = ?, patient_id = ?, vax_id = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                Timestamp.valueOf(application.getDateTime()).toString(),
                application.getPatientId(),
                application.getVaxId(),

                application.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM applications WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Transactional
    @Override
    public int deleteByPatient(Integer patientId, Integer id) {
        String sql = "DELETE FROM applications WHERE patient_id = ? AND id <> ?";

        return jdbcTemplate.update(sql, patientId, id);
    }

    @Override
    public List<Application> searchApplications(String query) {
        String sql =
                "SELECT n.id, n.date_time, n.patient_id, n.vax_id " +
                        "FROM applications n " +
                        "JOIN users u ON n.patient_id = u.id " +
                        "WHERE u.first_name LIKE ? OR u.last_name LIKE ? OR u.jmbg LIKE ? " +
                        "ORDER BY n.id";


        ApplicationRepository.ApplicationRowCallBackHandler rowCallbackHandler = new ApplicationRepository.ApplicationRowCallBackHandler();
        String likeQuery = "%" + query + "%";
        jdbcTemplate.query(sql, rowCallbackHandler, likeQuery, likeQuery, likeQuery);

        return rowCallbackHandler.getApplications();
    }
}
