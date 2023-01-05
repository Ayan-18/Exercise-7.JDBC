package com.example.exerciseseven.dao;

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
public class MeterGroupDao {

    private final JdbcTemplate jdbcTemplate;

    public boolean existsByName(String name) {
        String query = "select count(mg.id) from meter_group mg where mg.name = ?";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(query, Long.class, name)) > 0;
    }

    public List<MeterGroup> findAll() {
        return jdbcTemplate.query("select mg.* from meter_group mg order by mg.name", new MeterGroupRowMapper());
    }

    public Long findById(String name) {
        String query = "SELECT id FROM meter_group where name =?";
        return Objects.requireNonNull(jdbcTemplate.queryForObject(query, Long.class, name));
    }

    public void save(MeterGroup meterGroup) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meter_group")
                .usingGeneratedKeyColumns("id");
        jdbcInsert.execute(Map.of(
                "name", meterGroup.getName()
        ));
    }

    private static class MeterGroupRowMapper implements RowMapper<MeterGroup> {

        @Override
        public MeterGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            MeterGroup meterGroup = new MeterGroup();
            meterGroup.setId(rs.getLong("id"));
            meterGroup.setName(rs.getString("name"));
            return meterGroup;
        }
    }
}
