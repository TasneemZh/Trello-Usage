package browsers;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class OpenBrowser {
	public static WebDriver setChromeOptions() {
		String downloadFilePath = "src\\test\\resources\\downloads";
		File file = new File(downloadFilePath);

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", file.getAbsolutePath());
		System.out.println("file.getAbsolutePath(): " + file.getAbsolutePath());

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--start-maximized");
		options.addArguments("--headless");
		options.addArguments("--disable-infobars");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--ignore-certificate-errors");
		options.addArguments("--allow-running-insecure-content");


		WebDriver webDriver = new ChromeDriver(options);
		return webDriver;
	}

	public static WebDriver setFirefoxProfile() {
		String downloadFilepath = "downloads";
		File file = new File(downloadFilepath);
		FirefoxProfile profile = new FirefoxProfile();

		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.dir", file.getAbsolutePath());
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"text/csv,application/java-archive, application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/vnd.microsoft.portable-executable");

		FirefoxOptions option = new FirefoxOptions();
		option.setProfile(profile);

		WebDriver webDriver = new FirefoxDriver(option);
		return webDriver;
	}

	public WebDriver createDriver(String browser) {
		WebDriver webDriver;
		if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", "./src/main/resources/drivers/chromedriver.exe");
			webDriver = OpenBrowser.setChromeOptions();
		} else if (browser.equals("edge")) {
			System.setProperty("webdriver.edge.driver", "./src/main/resources/drivers/msedgedriver.exe");
			webDriver = new EdgeDriver();
		} else if (browser.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", "./src/main/resources/drivers/geckodriver.exe");
			webDriver = OpenBrowser.setFirefoxProfile();
		} else {
			return null;
		}
		return webDriver;
	}
}