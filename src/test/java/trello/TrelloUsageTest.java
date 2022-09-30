package trello;

import java.io.FileNotFoundException;
import java.time.Duration;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import browsers.OpenBrowser;
import files.ManageCsv;
import pages.Board;
import pages.CardDetails;
import pages.SignIn;

public class TrelloUsageTest {
	WebDriver driver;
	OpenBrowser browserType;
	String firstTab;
	String secondTab;
	ManageCsv file;
	Board board;
	CardDetails card;
	String boardTitle;
	String cardTitle;
	String cardDescription;
	String secondCardTitle;

	final String uploadFilePath = "src/test/resources/upload.csv";
	final String downloadFilePath = "src/test/resources/downloads/upload.csv";

	public TrelloUsageTest() {
		this.browserType = new OpenBrowser();
		this.file = new ManageCsv();
	}

	@BeforeTest
	@Parameters({ "browser" })
	public void prepareTestEnvironment(String browser) {
		this.driver = this.browserType.createDriver(browser);
//		this.driver.manage().window().maximize();
		this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		this.driver.get("https://trello.com/login");

		this.firstTab = this.driver.getWindowHandle();

		this.file.writeDataToUpload(uploadFilePath);

		SignIn signIn = new SignIn(this.driver);
		signIn.loginEmailPart("healthie.temp.email@gmail.com");
		signIn.loginPasswordPart("RegularUserForTesting123");
	}

	@Test(enabled = true)
	@Parameters({ "boardTitle", "cardTitle", "cardDescription" })
	public void testDownloadFile(String boardTitle, String cardTitle, String cardDescription) throws InterruptedException {
		this.boardTitle = boardTitle;
		this.cardTitle = cardTitle;
		this.cardDescription = cardDescription;

		this.board = new Board(this.driver);

		this.board.createNewBoard();
		this.board.createBoardMenu();
		this.board.nameBoard(this.boardTitle);
		this.board.createCardWithTitle(this.cardTitle);
		this.board.clickOnCard(this.cardTitle);

		this.card = new CardDetails(this.driver);

		this.card.writeCardDescription(this.cardDescription);
		this.card.uploadFile(uploadFilePath);
		this.card.downloadFile();

		WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.javaScriptThrowsNoExceptions(this.file.readFile(downloadFilePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(this.file.getFileNumnerOfRows(downloadFilePath, true),
				this.file.getFileNumnerOfRows(uploadFilePath, false));
	}

	@Test(enabled = true, dependsOnMethods = { "testDownloadFile" })
	public void testFileContent() throws InterruptedException {
		int numOfRows = this.file.getFileNumnerOfRows(downloadFilePath, true);
		for (int i = 0; i < numOfRows; i++) {
			Assert.assertEquals(this.file.getFileValue(i, true), this.file.getFileValue(i, false));
		}
		this.card.closeCard();
	}
		
	@Test(enabled = true, dependsOnMethods = { "testDownloadFile", "testFileContent" })
	@Parameters({ "secondCardTitle" })
	public void testUserAction(String secondCardTitle) throws InterruptedException {
		this.secondCardTitle = secondCardTitle;
		
		String boardUrl = this.driver.getCurrentUrl();
		this.secondTab = this.driver.getWindowHandle();
		
		this.driver.switchTo().newWindow(WindowType.TAB);
		this.driver.get(boardUrl);
		this.driver.switchTo().window(this.secondTab);
		this.driver.switchTo().window(firstTab);

		this.board.createCardWithTitle(this.secondCardTitle);

		this.driver.switchTo().window(this.secondTab);
		this.driver.navigate().refresh();

		Assert.assertTrue(this.board.checkCardExistence(this.secondCardTitle));

		this.driver.switchTo().window(firstTab);

		this.board.clickOnCard(this.cardTitle);

		this.card.copyPasteDescription();
		this.card.saveComment();

		Assert.assertEquals(this.card.getCommentText(this.cardDescription), this.cardDescription);
	}
	
	@Test(enabled = true, dependsOnMethods = { "testDownloadFile", "testFileContent", "testUserAction" })
	public void testDeletingCard() throws InterruptedException {
		this.card.closeCard();

		this.board.clickOnCard(this.secondCardTitle);

		this.card.clickOnHyperLinkButton("Archive");
		this.card.clickOnHyperLinkButton("Delete");
		this.card.clickOnInputButton("Delete");

		this.driver.switchTo().window(this.secondTab);
		this.driver.navigate().refresh();

		try {
			this.board.checkCardExistence(this.secondCardTitle);
		} catch (NoSuchElementException error) {
			Assert.assertTrue(true);
		}
	}

	@AfterTest
	public void clearTestSession() throws InterruptedException {
		this.driver.switchTo().window(firstTab);

		this.board.hoverOverMenu(this.boardTitle);
		this.board.openDropDownMenu();
		this.board.clickOnButtonByText("Close board...");
		this.board.clickOnButtonByText("Close");
		this.board.deleteBoard();
		
		this.driver.close();
	}

	@AfterSuite
	public void shutDown() {
		this.driver.quit();
	}
}
