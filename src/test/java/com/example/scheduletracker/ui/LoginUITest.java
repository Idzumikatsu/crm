package com.example.scheduletracker.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginUITest {

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
    void loginAsManagerRedirectsToManager() {
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys("manager");
        driver.findElement(By.id("password")).sendKeys("manager");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(driver.getCurrentUrl().endsWith("/manager"));
        assertTrue(driver.getPageSource().contains("Кабинет менеджера"));
    }

    @Test
    void loginAsTeacherRedirectsToTeacher() {
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys("teacher");
        driver.findElement(By.id("password")).sendKeys("teacher");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(driver.getCurrentUrl().endsWith("/teacher"));
        assertTrue(driver.getPageSource().contains("Мои занятия"));
    }
}
