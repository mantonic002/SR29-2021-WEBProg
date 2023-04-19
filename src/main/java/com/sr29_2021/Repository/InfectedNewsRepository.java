package com.sr29_2021.Repository;

import com.sr29_2021.Model.InfectedNews;
import com.sr29_2021.Repository.Interfaces.IInfectedNewsRepository;
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
public class InfectedNewsRepository implements IInfectedNewsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class InfectedNewsRowCallBackHandler implements RowCallbackHandler {

        private Map<Integer, InfectedNews> NewsMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Integer id = resultSet.getInt(index++);
            Integer infected = resultSet.getInt(index++);
            Integer tested = resultSet.getInt(index++);
            Integer hospitalized = resultSet.getInt(index++);
            Integer onRespirator = resultSet.getInt(index++);
            Integer infectedAllTime = resultSet.getInt(index++);
            LocalDateTime dateTime = resultSet.getTimestamp(index++).toLocalDateTime();

            InfectedNews news = NewsMap.get(id);
            if (news == null) {
                news = new InfectedNews(id, infected, tested, hospitalized, onRespirator, infectedAllTime, dateTime);
                NewsMap.put(news.getId(), news); // dodavanje u kolekciju
            }
        }

        public List<InfectedNews> getNews() {
            return new ArrayList<>(NewsMap.values());
        }

    }

    @Override
    public InfectedNews findOne(Integer id) {
        String sql =
                "SELECT n.id, n.infected, n.tested, n.hospitalized, n.on_respirator, get_total_infected(n.id), n.date_time " +
                        "FROM infected_news n " +
                        "WHERE n.id = ? " +
                        "ORDER BY n.id";

        InfectedNewsRepository.InfectedNewsRowCallBackHandler rowCallbackHandler = new InfectedNewsRepository.InfectedNewsRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        if (rowCallbackHandler.getNews().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getNews().get(0);
    }

    @Override
    public List<InfectedNews> findAll() {
        String sql =
                "SELECT n.id, n.infected, n.tested, n.hospitalized, n.on_respirator, get_total_infected(n.id), n.date_time " +
                        "FROM infected_news n " +
                        "ORDER BY n.id";

        InfectedNewsRepository.InfectedNewsRowCallBackHandler rowCallbackHandler = new InfectedNewsRepository.InfectedNewsRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if (rowCallbackHandler.getNews().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getNews();
    }

    @Transactional
    @Override
    public int save(InfectedNews infectedNews) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO infected_news (infected, tested, hospitalized, on_respirator, date_time) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setInt(index++, infectedNews.getInfected());
                preparedStatement.setInt(index++, infectedNews.getTested());
                preparedStatement.setInt(index++, infectedNews.getHospitalized());
                preparedStatement.setInt(index++, infectedNews.getOnRespirator());
                Timestamp timestamp = Timestamp.valueOf(infectedNews.getDateTime());
                preparedStatement.setString(index++, timestamp.toString());
                return preparedStatement;
            }

        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(InfectedNews infectedNews) {
        String sql = "UPDATE infected_news SET infected = ?, tested = ?, hospitalized = ?, on_respirator = ?, date_time = ? WHERE id = ?";
        boolean success = jdbcTemplate.update(sql,
                infectedNews.getInfected(),
                infectedNews.getTested(),
                infectedNews.getHospitalized(),
                infectedNews.getOnRespirator(),
                Timestamp.valueOf(infectedNews.getDateTime().toString()),
                infectedNews.getId()) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Integer id) {
        String sql = "DELETE FROM infected_news WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
