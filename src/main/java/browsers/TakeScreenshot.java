package browsers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TakeScreenshot {
	WebDriver driver;

	public TakeScreenshot(WebDriver driver) {
		this.driver = driver;
	}

	public void takeScreenshot(String imageName) throws IOException {
		TakesScreenshot screenshot = ((TakesScreenshot) this.driver);

		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
		File destFile = new File("./src/test/resources/screenshots/" + imageName);

		FileUtils.copyFile(srcFile, destFile);
	}
}
