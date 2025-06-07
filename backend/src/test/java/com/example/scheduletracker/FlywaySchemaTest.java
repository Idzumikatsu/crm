package com.example.scheduletracker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class FlywaySchemaTest {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Test
  public void tablesCreated() {
    // Flyway should have created the 'users' table in H2
    Integer userCount = jdbcTemplate.queryForObject("SELECT count(*) FROM users", Integer.class);
    assertThat(userCount).isZero();

    Integer groupCount = jdbcTemplate.queryForObject("SELECT count(*) FROM groups", Integer.class);
    assertThat(groupCount).isZero();

    Integer colExists =
        jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'LESSON' AND COLUMN_NAME = 'GROUP_ID'",
            Integer.class);
    assertThat(colExists).isEqualTo(1);
  }
}
