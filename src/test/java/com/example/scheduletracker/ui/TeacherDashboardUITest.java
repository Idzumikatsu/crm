package com.example.scheduletracker.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherDashboardUITest {

    @LocalServerPort
    int port;

    WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new HtmlUnitDriver(true);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void teacherPageShowsLessonsTable() {
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys("teacher");
        driver.findElement(By.id("password")).sendKeys("teacher");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(driver.getCurrentUrl().endsWith("/teacher"));
        assertTrue(driver.getPageSource().contains("Мои занятия"));
    }
}
