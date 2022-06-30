package com.qa.opencart.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ElementUtil;

import io.qameta.allure.Step;

public class LoginPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	// 1. private By locators: OR
	private By emailId = By.id("input-email");
	private By password = By.id("input-password");
	private By loginBtn = By.xpath("//input[@value='Login']");
	private By forgotPwdLink = By.linkText("Forgotten Password");
	private By registerLink = By.linkText("Register");
	private By accLogoutMessage = By.cssSelector("div#content h1");
	
	public static Logger log = Logger.getLogger(LoginPage.class);


	// 2. public page class const...
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(this.driver);
	}

	// 3. public page actions/methods:
	@Step("getting login page title....")
	public String getLoginPageTitle() {
		String title = eleUtil.waitForTitleIs(Constants.LOGIN_PAGE_TITLE, Constants.DEFAULT_TIME_OUT);
		System.out.println("login page title is: " + title);
		log.info("login page title is:" + title);
		return title;
	}

	@Step("getting login page url....")
	public String getLoginPageURL() {
		String url = eleUtil.waitForUrlContains(Constants.LOGIN_PAGE_URL_FRACTION, Constants.DEFAULT_TIME_OUT);
		System.out.println("login page url is: " + url);
		return url;
	}

	@Step("checking forgot pwd link exist or not....")
	public boolean isForgotPwdLinkExist() {
		return eleUtil.waitForElementVisible(forgotPwdLink, Constants.DEFAULT_ELEMENT_WAIT_TIME_OUT).isDisplayed();
	}

	@Step("checking register link exist or not....")
	public boolean isRegisterLinkExist() {
		return eleUtil.waitForElementVisible(registerLink, Constants.DEFAULT_ELEMENT_WAIT_TIME_OUT).isDisplayed();
	}

	@Step("login with username {0} and password {1}")
	public AccountsPage doLogin(String username, String pwd) {
		System.out.println(username + ":"+ pwd);
		eleUtil.waitForElementVisible(emailId, Constants.DEFAULT_ELEMENT_WAIT_TIME_OUT).sendKeys(username);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginBtn);

		return new AccountsPage(driver);
	}

	@Step("getting success messg after logout")
	public String getLogoutMessage() {
		String logoutMesg = eleUtil.waitForElementVisible(accLogoutMessage, Constants.DEFAULT_ELEMENT_WAIT_TIME_OUT)
				.getText();
		System.out.println("Logout successful mesg === " + logoutMesg);
		return logoutMesg;
	}
	
	@Step("navigating to register page...")
	public RegisterPage navigateToRegisterPage() {
		eleUtil.doClick(registerLink);
		return new RegisterPage(driver);
	}
	
	
	
	
	
	
	
	
	
	

}
