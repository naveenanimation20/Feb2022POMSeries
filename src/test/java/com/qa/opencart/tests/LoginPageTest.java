package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.pages.AccountsPage;
import com.qa.opencart.utils.Constants;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("EPIC -100: Design login page for open cart application")
@Story("US - 101: design login page features")
public class LoginPageTest extends BaseTest {

	@Description("Verify login Page Title Test")
	@Severity(SeverityLevel.NORMAL)
	@Test(priority = 1)
	public void loginPageTitleTest() {
		String actualTitle = loginPage.getLoginPageTitle();
		Assert.assertEquals(actualTitle, Constants.LOGIN_PAGE_TITLE);
	}
	
	@Description("Verify login Page header Test")
	@Severity(SeverityLevel.NORMAL)
	@Test(priority = 6)
	public void loginPageHeaderTest() {
		//loginPage.getLoginPageTitle();
		String actualTitle = "Open Cart";
		Assert.assertEquals(actualTitle, "Open Cart");
	}
	
	
	@Description("Verify login Page url Test")
	@Severity(SeverityLevel.MINOR)
	@Test(priority = 2)
	public void loginPageUrlTest() {
		String actualUrl = loginPage.getLoginPageURL();
		Assert.assertTrue(actualUrl.contains(Constants.LOGIN_PAGE_URL_FRACTION));
	}

	@Description("Verify forgot pwd link exist on the login page")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 3)
	public void forgotPwdLinkExistTest() {
		Assert.assertTrue(loginPage.isForgotPwdLinkExist());
	}

	
	@Description("Verify register link on the login page")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority = 4)
	public void registerLinkExistTest() {
		Assert.assertTrue(loginPage.isRegisterLinkExist());
	}

	@Description("Verify user is able to login to the applicaiton with correct credentials")
	@Severity(SeverityLevel.BLOCKER)
	@Test(priority = 5)
	public void loginTest() {
		AccountsPage accPage = loginPage.doLogin(prop.getProperty("username").trim(), prop.getProperty("password").trim());
		String accPageTitle = accPage.getAccPageTitle();
		System.out.println(accPageTitle);
		Assert.assertEquals(accPageTitle, Constants.ACCOUNTS_PAGE_TITLE);
		Assert.assertTrue(accPage.isLogoutLinkExist());
	}

}
