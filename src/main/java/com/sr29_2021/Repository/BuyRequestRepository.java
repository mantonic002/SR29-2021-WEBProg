package com.sr29_2021.Repository;

import com.sr29_2021.Model.*;
import com.sr29_2021.Repository.Interfaces.*;
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
public class BuyRequestRepository implements IBuyRequestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IVaxRepository vaxRepo;

    private class BuyRequestRowCallBackHandler implements RowCallbackHandler {

        private Map<Integer, BuyRequest> BuyRequestMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            Integer amount = resultSet.getInt(index++);
            String reason = resultSet.getString(index++);
            LocalDateTime dateTime = resultSet.getTimestamp(index++).toLocalDateTime();
            String denialComment = resultSet.getString(index++);

            Integer userId = resultSet.getInt(index++);
            Integer vaxId = resultSet.getInt(index++);

            User user = userRepository.findOne(userId);
            Vax vax = vaxRepo.findOne(vaxId);


            BuyRequest buyRequest = BuyRequestMap.get(id);
            if (buyRequest == null) {
                buyRequest = new BuyRequest(id, amount, reason, dateTime.minusHours(2), denialComment, user, vax);
                BuyRequestMap.put(buyRequest.getId(), buyRequest); // dodavanje u kolekciju
            }
        }

        public List<BuyRequest> getBuyRequests() {
            return new ArrayList<>(BuyRequestMap.values());
        }

    }

    @Override
    public BuyRequest findOne(Integer id) {
        String sql =
                "SELECT * " +
                        "FROM buy_request n " +
                        "WHERE n.id = ? " +
                        "ORDER BY n.id";

        BuyRequestRepository.BuyRequestRowCallBackHandler rowCallbackHandler = new BuyRequestRepository.BuyRequestRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (rowCallbackHandler.getBuyRequests().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getBuyRequests().get(0);
    }

    @Override
    public List<BuyRequest> findAll() {
        String sql =
                "SELECT * " +
                        "FROM buy_request n " +
                        "ORDER BY n.id";

        BuyRequestRepository.BuyRequestRowCallBackHandler rowCallbackHandler = new BuyRequestRepository.BuyRequestRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getBuyRequests().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getBuyRequests();
    }

    @Transactional
    @Override
    public int save(BuyRequest buyRequest) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO buy_request (amount, reason, date_time, denial_comment, staff_id, vax_id) VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setInt(index++, buyRequest.getAmount());
                preparedStatement.setString(index++, buyRequest.getReason());
                Timestamp timestamp = Timestamp.valueOf(buyRequest.getDate());
                preparedStatement.setString(index++, timestamp.toString());
                preparedStatement.setString(index++, buyRequest.getDenialComment());
                preparedStatement.setInt(index++, buyRequest.getStaffId());
                preparedStatement.setInt(index++, buyRequest.getVaxId());
                return preparedStatement;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }
    @Transactional
    @Override
    public int update(BuyRequest buyRequest) {
        String sql = "UPDATE buy_request SET amount = ?, reason = ?, date_time = ?, denial_comment = ?, staff_id = ?, vax_id = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                buyRequest.getAmount(),
                buyRequest.getReason(),
                Timestamp.valueOf(buyRequest.getDate()).toString(),
                buyRequest.getDenialComment(),
                buyRequest.getStaffId(),
                buyRequest.getVaxId(),
                buyRequest.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM buy_request WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
