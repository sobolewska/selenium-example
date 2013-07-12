package com.company.selenium.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

public class MyFirstTest extends MethodsForTestsAbstract {

  /**
   * Note: For JUnit 3 names of test classes should end with "Test" and test
   * methods names should start with "test". For JUnit 4 it's not necessary, but
   * make the code clean and simple to read.
   */
  @Test
  public void testToPassApp() {
    openUrl("http://www.google.pl");
    se.findElement(By.id("gbqfq")).sendKeys("selenium");
    clickAndWait("gbqfb");
    assertTrue(se.findElement(By.id("rso")).findElements(By.tagName("li")).get(0).getText().toLowerCase()
        .contains("selenium - web browser automation"));
  }

  @Test
  public void testToFail() {
    openUrl("http://www.google.pl");
    assertTrue(false);
  }

}
