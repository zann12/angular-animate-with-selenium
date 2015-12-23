package com.us.validators.test;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;

/**
 * Base class to extends when developing a Test. It provides some basic APIs:
 * assertions, browser manipulation.
 */
@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
		MethodListener.class })
public class QuickTest {

	private WebDriver singleDriver; // Single driver for all tests.
		
	@BeforeClass
  public void setUp() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.singleDriver = new ChromeDriver();
  }
	
	@Test(priority = 1, invocationCount = 100)
	public void validatorsOnQuickTest() {
		this.singleDriver.get("http://localhost:10000/Validator_bug/app/index.html#/view1");
		WebElement t = this.singleDriver.findElement(By.id("input#id1"));
		WebElement tMax = this.singleDriver.findElement(By.id("input#id2"));
		QuickTest.clearInputValue(tMax);
		tMax.sendKeys("10");
		QuickTest.clearInputValue(t);
		t.sendKeys("aaaaaaaaaaa");
		Assert.assertTrue(this.hasError(t));  // "aaaaaaaaaaa".length > maxLength (10) => Invalid is expected
	}
	
	private boolean hasError(WebElement element) {
    /*
     * An error must be visible on a invalid component only when:
     * #1. The 'ng-invalid' CSS class is set by angular
     * AND
     * (
     * #2. the model is not pristine (because in our case we don't need to show
     * 		the error if there is no action from the user).
     *  OR
     * #3. the ers-submitted is set too.
     * )
     */
    String classes = element.getAttribute("class");
    if (classes == null || classes.length() == 0) {
      return false;
    }
    // split classes to avoid conflict with names!
    String[] cssClasses = classes.split(" ");

    if (ArrayUtils.contains(cssClasses, "ng-invalid") // #1.
        && !ArrayUtils.contains(cssClasses, "ng-pristine") // #3.
    ) {
      return true;
    }
    return false;
  }

	private static void clearInputValue(WebElement input) {
    input.click();
    input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
    input.sendKeys(Keys.BACK_SPACE);
  }
	
}
