package com.company.selenium.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Shared methods.
 */
public abstract class MethodsForTestsAbstract {

  /*
   * For properties
   */
  private final static String PROPERTIES_FILE = "config.properties";

  /*
   * For selenium
   */
  public final static String BROWSER_TYPE = getProperty("BROWSER_TYPE", "firefox");
  public static WebDriver se;

  @Rule
  public ScreenshotTestRule screenshotTestRule = new ScreenshotTestRule();

  /********************************************************************************************
   * Set properties
   ********************************************************************************************/

  /**
   * Returns property value. If property is not found in properties file sets
   * the property value to propertyDefaultValue.
   * 
   * @param propertyName
   * @param propertyDefaultValue
   * @return
   */
  public static String getProperty(String propertyName, String propertyDefaultValue) {
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream(PROPERTIES_FILE));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    String out = prop.getProperty(propertyName, propertyDefaultValue);
    System.out.println("\t" + propertyName + " = " + out);
    return out.equals("empty") ? "" : out;
  }

  /**
   * Creates a selenium web driver browser instance. By default creates firefox
   * instance.
   * 
   * @param b
   * @return WebDriver browser
   */
  private static void getBrowser(String b) {

    /*
     * FIREFOX
     */
    if (b.toLowerCase().equals("firefox")) {

      FirefoxProfile fp = new FirefoxProfile();
      fp.setPreference("webdriver.load.strategy", "unstable");

      se = new FirefoxDriver(fp);
    }
    /*
     * CHROME
     */
    else if (b.toLowerCase().equals("chrome")) {
      File file = new File("resources/drivers/chromedriver.exe");
      System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

      se = new ChromeDriver();

      /*
       * Wiki: http://code.google.com/p/selenium/wiki/ChromeDriver. The latest
       * version can be downloaded from:
       * http://code.google.com/p/chromedriver/downloads/list
       */
    }
    /*
     * INTERNET EXPLORER 64
     */
    else if (b.toLowerCase().equals("ie64")) {
      File file = new File("resources/drivers/IEDriverServer_64.exe");
      System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
      DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
      caps.setCapability("ignoreZoomSetting", true);
      caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

      se = new InternetExplorerDriver(caps);

      /*
       * Wiki: http://code.google.com/p/selenium/wiki/InternetExplorerDriver.
       * The latest version can be downloaded from:
       * http://code.google.com/p/selenium/downloads/list
       */
    }
    /*
     * INTERNET EXPLORER 32
     */
    else if (b.toLowerCase().equals("ie32")) {
      File file = new File("resources/drivers/IEDriverServer_32.exe");
      System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
      DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
      caps.setCapability("ignoreZoomSetting", true);
      caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

      se = new InternetExplorerDriver(caps);

      /*
       * Wiki: http://code.google.com/p/selenium/wiki/InternetExplorerDriver.
       * The latest version can be downloaded from:
       * http://code.google.com/p/selenium/downloads/list
       */
    } else {

      FirefoxProfile fp = new FirefoxProfile();
      fp.setPreference("webdriver.load.strategy", "unstable");

      se = new FirefoxDriver(fp);

    }
  }

  /********************************************************************************************
   * Selenium test methods
   ********************************************************************************************/

  /**
   * Opens a page with given url and then waits 2 seconds
   * 
   * @param url
   */
  public void openUrl(String url) {
    se.get(url);
    waitNsec(2);
  }

  /**
   * Clicks element and then waits 2 seconds
   * 
   * @param locator
   * @param timeToWaitInSec
   */
  public void clickAndWait(String id) {
    se.findElement(By.id(id)).click();
    waitNsec(2);
  }

  /**
   * Clicks element and then waits n seconds
   * 
   * @param locator
   * @param timeToWaitInSec
   */
  public void clickAndWait(String id, int timeToWaitInSec) {
    se.findElement(By.id(id)).click();
    waitNsec(timeToWaitInSec);
  }

  /**
   * Waits n seconds
   * 
   * @param n
   */
  public static void waitNsec(int n) {
    long t0, t1;
    t0 = System.currentTimeMillis();
    do {
      t1 = System.currentTimeMillis();
    } while (t1 - t0 < (n * 1000));
  }

  /********************************************************************************************
   * JUnit test methods
   ********************************************************************************************/

  /**
   * This will open browser and maximize windowWill be done at the beginning of
   * tests set in one class.
   */
  @BeforeClass
  public static void before() {

    getBrowser(BROWSER_TYPE);
    se.manage().window().maximize();

  }

  /**
   * This will close browser. Will be done at the end of tests set in one class.
   */
  @AfterClass
  public static void after() {

    if (!BROWSER_TYPE.toLowerCase().equals("ie64") && !BROWSER_TYPE.toLowerCase().equals("ie32")) {
      se.close();
    }
    se.quit();
    /**
     * To shutdown form browser use:
     * http://localhost:4444/selenium-server/driver/?cmd=shutDownSeleniumServer
     */
  }

  /**
   * To make a screenshot on fail, will save it in
   * target/surefire-reports/screenshots as ClassName_methodName.png.
   */
  class ScreenshotTestRule implements TestRule {
    public Statement apply(final Statement statement, final Description description) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          try {
            statement.evaluate();
          } catch (Throwable t) {
            takeScreenshot(getName(description.getClassName(), description.getMethodName()));
            throw t;
            // rethrow to allow the failure to be reported to JUnit
          }
        }
      };
    }

    private String getName(String className, String methodName) {
      return className.substring(className.lastIndexOf(".") + 1) + "_" + methodName;
      // ClassName_methodName
    }

    private void takeScreenshot(String name) {
      try {
        new File("target/surefire-reports/screenshots/").mkdirs();
        // check that folder exists
        FileOutputStream out = new FileOutputStream("target/surefire-reports/screenshots/" + name + ".png");
        out.write(((TakesScreenshot) se).getScreenshotAs(OutputType.BYTES));
        out.close();
        System.out.println("Screenshot saved (" + name + ".png).");
      } catch (Exception e) {
        System.err.println("Failed to save screenshot (" + name + ".png).");
        // No need to crash the tests if the screenshot fails
      }
    }
  }

}