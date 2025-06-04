package com.example.scheduletracker;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FlywaySchemaTest {
    @Autowired
    private DataSource dataSource;

    @Test
    void tablesCreated() throws Exception {
        DatabaseMetaData meta = dataSource.getConnection().getMetaData();
        try (var rs = meta.getTables(null, null, "USERS", null)) {
            assertTrue(rs.next(), "users table exists");
        }
        try (var rs = meta.getTables(null, null, "TEACHER_STUDENTS", null)) {
            assertTrue(rs.next(), "teacher_students table exists");
        }
    }
}
