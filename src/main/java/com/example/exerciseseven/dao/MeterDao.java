package com.example.exerciseseven.dao;

import com.example.exerciseseven.entity.Meter;
import com.example.exerciseseven.entity.MeterGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MeterDao {

    private final JdbcTemplate jdbcTemplate;

    public boolean existsById(long meterId) {
        String query = "select count(m.id) from meter m where m.id = ?";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(query, Long.class, meterId)) > 0;
    }

    public List<Meter> findAllByMeterGroupId(long meterGroupId) {
        return jdbcTemplate.query("select m.* from meter m where m.meter_group_id = ?", new MeterDao.MeterRowMapper(), meterGroupId);
    }

    public void save(Meter meter) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meter");
        jdbcInsert.execute(Map.of(
                "id", meter.getId(),
                "type", meter.getType(),
                "meter_group_id", meter.getMeterGroupId()
        ));
    }

    private static class MeterRowMapper implements RowMapper<Meter> {

        @Override
        public Meter mapRow(ResultSet rs, int rowNum) throws SQLException {
            Meter meter = new Meter();
            meter.setId(rs.getLong("id"));
            meter.setType(rs.getString("type"));
            meter.setMeterGroupId(rs.getLong("meter_group_id"));
            return meter;
        }
    }
}
