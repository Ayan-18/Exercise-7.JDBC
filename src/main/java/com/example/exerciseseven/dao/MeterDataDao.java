package com.example.exerciseseven.dao;

import com.example.exerciseseven.entity.Meter;
import com.example.exerciseseven.entity.MeterData;
import com.example.exerciseseven.entity.MeterGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MeterDataDao {
    private final JdbcTemplate jdbcTemplate;

    public MeterData findFirstByMeterIdAndDate(long meterId, LocalDate date) {
        String query = "select md.* from meter_data md where md.meter_id = ? and md.date_of_data >= ? order by md.date_of_data limit 1";
        return jdbcTemplate.queryForObject(query, new MeterDataRowMapper(), meterId, date);
    }

    public MeterData findLastByMeterIdAndDate(long meterId, LocalDate date) {
        String query = "select md.* from meter_data md where md.meter_id = ? and md.date_of_data <= ?::timestamp + interval '23 hour' order by md.date_of_data desc limit 1";
        return jdbcTemplate.queryForObject(query, new MeterDataRowMapper(), meterId, date);
    }

    public List<MeterData> findAllByMeterId(long meterId) {
        return jdbcTemplate.query("select md.* from meter_data md where md.meter_id = ?", new MeterDataDao.MeterDataRowMapper(), meterId);
    }

    public void save(MeterData meterData) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meter_data")
                .usingGeneratedKeyColumns("id");
        jdbcInsert.execute(Map.of(
                "meter_id", meterData.getMeterId(),
                "date_of_data", meterData.getDateOfData(),
                "reading", meterData.getReading()
        ));
    }

    private static class MeterDataRowMapper implements RowMapper<MeterData> {

        @Override
        public MeterData mapRow(ResultSet rs, int rowNum) throws SQLException {
            MeterData meterData = new MeterData();
            meterData.setId(rs.getLong("id"));
            meterData.setMeterId(rs.getLong("meter_id"));
            meterData.setDateOfData(rs.getTimestamp("date_of_data").toLocalDateTime());
            meterData.setReading(rs.getDouble("reading"));
            return meterData;
        }
    }
}
