package com.excilys.computerdatabase;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;



public class WebUITest {

	private WebDriver driver;
	
	@Before
	public void init() {
        driver = new FirefoxDriver();
	}
	
	@Test
	public void test() {
        driver.get("http://localhost:8080/computer-database/dashboard");

        driver.findElement(By.id("addComputer"));
	}
	
	@Test
	public void pagination() {
		int x = 20;
		driver.get("http://localhost:8080/computer-database/dashboard?nbResults=" + x);
		assertEquals(x,driver.findElements(By.cssSelector("#results tr")).size());
	}
	
	@Test
	public void addComputer() {
		driver.get("http://localhost:8080/computer-database/addComputer");

        WebElement element = driver.findElement(By.name("computerName"));
        element.sendKeys("Selenium");
        
        driver.findElement(By.name("introducedDate")).sendKeys("2000-02-10");
        driver.findElement(By.name("discontinuedDate")).sendKeys("2012-02-10");
        Select select = new Select(driver.findElement(By.name("companyId")));
        select.selectByIndex(1);
        
        element.submit();
	}
	
	@Test
	public void editComputer() {
		driver.get("http://localhost:8080/computer-database/editComputer?id=580");

        WebElement element = driver.findElement(By.name("computerName"));
        element.sendKeys("Selenium Edited");
        
        driver.findElement(By.name("introducedDate")).sendKeys("2003-02-10");
        driver.findElement(By.name("discontinuedDate")).sendKeys("2015-02-10");
        Select select = new Select(driver.findElement(By.name("companyId")));
        select.selectByIndex(2);
        
        element.submit();
	}
	
	@After
	public void after() {
		 //Close the browser
        driver.quit();
	}

}
