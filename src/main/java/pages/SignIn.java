package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignIn {
	WebDriver driver;

	public SignIn(WebDriver driver) {
		this.driver = driver;
	}

	public void loginEmailPart(String email) {
		WebElement emailField = this.driver.findElement(By.id("user"));
		emailField.sendKeys(email);

		WebElement continueBtn = this.driver.findElement(By.id("login"));
		continueBtn.click();
	}

	public void loginPasswordPart(String password) {
		WebElement passwordField = this.driver.findElement(By.id("password"));

		try {
			WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(passwordField));
			passwordField.sendKeys(password);
		} catch(StaleElementReferenceException e) {
			this.driver.findElement(By.id("password")).sendKeys(password);
		}

		WebElement signInBtn = this.driver.findElement(By.id("login-submit"));
		signInBtn.click();
	}
}
