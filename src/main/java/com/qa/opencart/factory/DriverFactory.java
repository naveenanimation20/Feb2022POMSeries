package com.qa.opencart.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.qa.opencart.customexception.FrameworkException;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * 
 * @author naveenautomationlabs
 *
 */
public class DriverFactory {

	WebDriver driver;
	Properties prop;
	OptionsManager optionsManager;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	/**
	 * this method is used to initialize the driver on the basis of given
	 * browsername
	 * 
	 * @param properties prop
	 * @return this method will return the webdriver
	 */
	public WebDriver init_driver(Properties prop) {

		 String browserName = prop.getProperty("browser").trim();

		//String browserName = System.getProperty("browser");

		System.out.println("browser name is : " + browserName);

		optionsManager = new OptionsManager(prop);

		if (browserName.equalsIgnoreCase("chrome")) {

			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// remote execution:
				init_remoteDriver("chrome");
			} else {
				// local execution:
				WebDriverManager.chromedriver().setup();
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}

		} else if (browserName.equalsIgnoreCase("firefox")) {
			
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				// remote execution:
				init_remoteDriver("firefox");
			}else {
				//local execution:
				WebDriverManager.firefoxdriver().setup();
				tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			}
		} 
		
		else if (browserName.equalsIgnoreCase("safari")) {
			tlDriver.set(new SafariDriver());
		} 
		
		else {
			System.out.println("please pass the right browser name... " + browserName);
			throw new FrameworkException("no browser found...");
		}

		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		getDriver().get(prop.getProperty("url"));
		return getDriver();

	}

	private void init_remoteDriver(String browserName) {

		System.out.println("Running tests on remote grid server: " + browserName);

		if (browserName.equalsIgnoreCase("chrome")) {
			try {
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getChromeOptions()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (browserName.equalsIgnoreCase("firefox")) {
			try {
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("huburl")), optionsManager.getFirefoxOptions()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

	}

	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	/**
	 * This method is used to initialize the properties from the respective env
	 * config file
	 * 
	 * @return this returns properties class object with all the config properties
	 */
	public Properties init_prop() {
		FileInputStream ip = null;
		prop = new Properties();

		// mvn command line arg:
		// mvn clean install -Denv="qa"

		String envName = System.getProperty("env");
		System.out.println("Running tests on environment: " + envName);

		if (envName == null) {
			System.out.println("No env is given ..... hence running it on QA");
			try {
				ip = new FileInputStream("./src/test/resources/config/qa.config.properties");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		else {

			try {
				switch (envName.toLowerCase()) {
				case "qa":
					ip = new FileInputStream("./src/test/resources/config/qa.config.properties");
					break;
				case "dev":
					ip = new FileInputStream("./src/test/resources/config/dev.config.properties");
					break;
				case "stage":
					ip = new FileInputStream("./src/test/resources/config/stage.config.properties");
					break;
				case "uat":
					ip = new FileInputStream("./src/test/resources/config/uat.config.properties");
					break;
				case "prod":
					ip = new FileInputStream("./src/test/resources/config/config.properties");
					break;

				default:
					System.out.println("please pass the right environment value..." + envName);
					throw new FrameworkException("no env found...");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (FrameworkException e) {
				e.printStackTrace();
			}
		}

		try {
			prop.load(ip);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	/**
	 * take screenshot
	 * 
	 */

	public String getScreenshot() {
		File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/screenshot/" + System.currentTimeMillis() + ".png";
		File destination = new File(path);
		try {
			FileUtils.copyFile(srcFile, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	
	

}
