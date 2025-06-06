package com.example.scheduletracker.ui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled("UI tests disabled in CI")
class ManagerDashboardUITest {

  @LocalServerPort int port;

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
  void managerPageShowsAssignButton() {
    driver.get("http://localhost:" + port + "/login");
    driver.findElement(By.id("username")).sendKeys("manager");
    driver.findElement(By.id("password")).sendKeys("manager");
    driver.findElement(By.cssSelector("button[type='submit']")).click();

    assertTrue(driver.getCurrentUrl().endsWith("/manager"));
    assertTrue(driver.getPageSource().contains("Назначить"));
  }
}
