package pages;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import browsers.TakeScreenshot;

public class Board {
	WebDriver driver;
	WebDriverWait wait;
	TakeScreenshot screenshot;

	public Board(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
		this.screenshot = new TakeScreenshot(driver);
	}

	public void createNewBoard() throws InterruptedException {
		WebElement createBtn = this.driver.findElement(By.xpath("//button[@data-test-id='header-create-menu-button']"));
		createBtn.click();
		Thread.sleep(3000);
	}

	public void createBoardMenu() throws InterruptedException {
		WebElement menuBtn = this.driver.findElement(By.xpath("//button[@data-test-id='header-create-board-button']"));
		menuBtn.click();
		Thread.sleep(3000);
	}

	public void nameBoard(String boardName) throws InterruptedException {
		WebElement boardInput = this.driver.findElement(By.xpath("//input[@data-test-id='create-board-title-input']"));
		boardInput.sendKeys(boardName);
		Thread.sleep(3000);

		WebElement createBoardBtn = this.driver
				.findElement(By.xpath("//button[@data-test-id='create-board-submit-button']"));
		createBoardBtn.click();
		Thread.sleep(3000);
	}

	public boolean checkCardExistence(String title) throws InterruptedException {
		Thread.sleep(3000);
		return this.driver.findElement(By.linkText(title)).isDisplayed();
	}

	public void createCardWithTitle(String title) throws InterruptedException {
		try {
			this.screenshot.takeScreenshot("before_" + title + "_creation.jpg");
			if (CardDetails.cardsCnt > 0) {
				WebElement addCardBtn = this.driver.findElement(By.linkText("Add a card"));
				addCardBtn.click();
				Thread.sleep(3000);
			} else {
				CardDetails.cardsCnt++;
			}

			Thread.sleep(3000);
//		WebElement titleInput = this.wait.until(ExpectedConditions.visibilityOf(
//				this.driver.findElement(By.xpath("//textarea[@placeholder='Enter a title for this card…']"))));
			WebElement titleInput = this.driver
					.findElement(By.xpath("//textarea[@class='list-card-composer-textarea js-card-title']"));
			titleInput.sendKeys(title);
			Thread.sleep(3000);

			WebElement createCardBtn = this.driver.findElement(By.xpath("//input[@value='Add card']"));
			createCardBtn.click();
			Thread.sleep(3000);
			this.screenshot.takeScreenshot("after_" + title + "_creation.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clickOnCard(String title) throws InterruptedException {
		List<WebElement> cardsBtn = this.driver.findElements(By.xpath("//span[@class='list-card-title js-card-name']"));
		for (int i = 0; i < cardsBtn.size(); i++) {
			if (cardsBtn.get(i).getText().equals(title)) {
				Thread.sleep(3000);
				cardsBtn.get(i).click();
				Thread.sleep(3000);
			}
		}
	}

	public void clickOnButtonByText(String btnText) throws InterruptedException {
		WebElement closeBoardBtn = this.driver.findElement(By.xpath("//button[@title='" + btnText + "']"));
		closeBoardBtn.click();
		Thread.sleep(3000);
	}

	public void hoverOverMenu(String boardTitle) throws InterruptedException {
		Thread.sleep(3000);
		WebElement boardMenu = this.driver.findElement(By.linkText(boardTitle));
		Actions action = new Actions(this.driver);

		action.moveToElement(boardMenu).perform();
		Thread.sleep(3000);
	}

	public void openDropDownMenu() throws InterruptedException {
		WebElement dropDownMenuBtn = this.driver.findElement(By.xpath("//button[@aria-label='Board actions menu']"));
		dropDownMenuBtn.click();
		Thread.sleep(3000);
	}

	public void deleteBoard() throws InterruptedException {
		Thread.sleep(3000);
		WebElement deleteBtnLink = this.driver
				.findElement(By.xpath("//button[@data-test-id='close-board-delete-board-button']"));
		deleteBtnLink.click();
		Thread.sleep(3000);

		WebElement deleteBtnConfirmation = this.driver
				.findElement(By.xpath("//button[@data-test-id='close-board-delete-board-confirm-button']"));
		deleteBtnConfirmation.click();
		Thread.sleep(3000);
	}
}
