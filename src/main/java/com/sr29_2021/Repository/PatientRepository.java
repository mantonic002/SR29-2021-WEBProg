package com.sr29_2021.Repository;

import com.sr29_2021.Model.Patient;
import com.sr29_2021.Model.User;
import com.sr29_2021.Repository.Interfaces.IPatientRepository;
import com.sr29_2021.Repository.Interfaces.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PatientRepository implements IPatientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IUserRepository userRepo;

    private class PatientRowCallbackHandler implements RowCallbackHandler {

        private Map<Integer, Patient> Patients = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            Boolean vaxxed = resultSet.getBoolean(index++);
            Integer recievedDoses = resultSet.getInt(index++);
            LocalDateTime lastDose = resultSet.getTimestamp(index++).toLocalDateTime();
            User user = userRepo.findOne(id);

            Patient patient = Patients.get(id);
            if (patient == null) {
                patient = new Patient(id, vaxxed, recievedDoses, lastDose, user);
                Patients.put(patient.getUserId(), patient);
            }
        }

        public List<Patient> getPatients() { return new ArrayList<>(Patients.values()); }
    }

    @Override
    public Patient findOne(Integer id) {
        String sql =
                "SELECT p.user_id, p.vaxxed, p.received_doses, p.last_dose_date " +
                        "FROM patients_info p " +
                        "WHERE p.user_id = ? " +
                        "ORDER BY p.user_id";

        PatientRepository.PatientRowCallbackHandler rowCallbackHandler = new PatientRepository.PatientRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if(rowCallbackHandler.getPatients().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getPatients().get(0);
    }

    @Override
    public List<Patient> findAll() {
        String sql =
                "SELECT p.user_id, p.vaxxed, p.received_doses, p.last_dose_date " +
                        "FROM patients_info p " +
                        "ORDER BY p.user_id";

        PatientRepository.PatientRowCallbackHandler rowCallbackHandler = new PatientRepository.PatientRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if(rowCallbackHandler.getPatients().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getPatients();
    }

    @Transactional
    @Override
    public int save(Patient patient) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO patients_info (user_id, vaxxed, received_doses, last_dose_date) values (?, ?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setInt(index++, patient.getUserId());
                preparedStatement.setBoolean(index++, patient.getVaxxed());
                preparedStatement.setInt(index++, patient.getReceivedDoses());
                Timestamp timestamp = Timestamp.valueOf(patient.getLastDoseDate());
                preparedStatement.setString(index++, timestamp.toString());

                return preparedStatement;
            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return  success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Patient patient) {
        String sql = "UPDATE patients_info SET vaxxed = ?, received_doses = ?, last_dose_date = ? WHERE user_id = ?";
        boolean success = jdbcTemplate.update(sql,
                patient.getVaxxed(),
                patient.getReceivedDoses(),
                patient.getLastDoseDate(),
                patient.getUserId()) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM patients_info WHERE user_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
