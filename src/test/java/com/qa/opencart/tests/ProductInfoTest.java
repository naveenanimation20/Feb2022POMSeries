package com.qa.opencart.tests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.pages.CommonsPage;
import com.qa.opencart.pages.ProductInfoPage;
import com.qa.opencart.utils.DescriptionConstants;

public class ProductInfoTest extends BaseTest {

	@BeforeClass
	public void productInforSetup() {
		commPage = new CommonsPage(driver);
		productInfoPage = new ProductInfoPage(driver);
	}
	
	
	@DataProvider
	public Object[][] getProductData() {
		return new Object[][] {
			{"Macbook", "MacBook Pro", 4},
			{"Macbook", "MacBook Air", 4},
			{"Samsung", "Samsung SyncMaster 941BW", 1}
		};
	}
	
	

	@Test(dataProvider = "getProductData")
	public void productImagesCountTest(String searchKey, String productName, int imagesCount) {
		searchResultsPage = commPage.doSearch(searchKey);
		productInfoPage = searchResultsPage.selectProductName(productName);
		Assert.assertEquals(productInfoPage.getProductImagesCount(), imagesCount);
	}
	
	
	@Test
	public void productDescriptionTest() {
		searchResultsPage = commPage.doSearch("Macbook");
		productInfoPage = searchResultsPage.selectProductName("MacBook Air");
		String productDesc = productInfoPage.getProductDescription();
		System.out.println("product desc: " + productDesc);
		
		softAssert.assertTrue(productDesc.contains("MacBook Air"));
		softAssert.assertTrue(productDesc!=null);
		softAssert.assertFalse(productDesc.isEmpty());
		softAssert.assertTrue(productDesc.contains(DescriptionConstants.MACBOOK_AIR_DESCRIPTION));
		softAssert.assertAll();
	}
	
	
	@Test
	public void productDataTest() {
		searchResultsPage = commPage.doSearch("Macbook");
		productInfoPage = searchResultsPage.selectProductName("MacBook Air");
		Map<String, String> actProductInfoMap = productInfoPage.getProductInfo();
		actProductInfoMap.forEach((k,v) -> System.out.println(k + ":" + v));//Brand:Apple
		
		softAssert.assertEquals(actProductInfoMap.get("Brand"), "Apple");
		softAssert.assertEquals(actProductInfoMap.get("Availability"), "In Stock");
		softAssert.assertEquals(actProductInfoMap.get("name"), "MacBook Air");
		softAssert.assertEquals(actProductInfoMap.get("Reward Points"), "700");
		softAssert.assertAll();

		
		//hashmap:
//		Brand:Apple
//		Availability:In Stock
//		price:$1,202.00
//		extaxprice:Ex Tax: $1,000.00
//		name:MacBook Air
//		Product Code:Product 17
//		Reward Points:700
		
		//LikedHashMap:
//		name:MacBook Air
//		Brand:Apple
//		Product Code:Product 17
//		Reward Points:700
//		Availability:In Stock
//		price:$1,202.00
//		extaxprice:Ex Tax: $1,000.00
		
		//TreeMap:
//		Availability:In Stock
//		Brand:Apple
//		Product Code:Product 17
//		Reward Points:700
//		extaxprice:Ex Tax: $1,000.00
//		name:MacBook Air
//		price:$1,202.00
		
		
		
	}
	
	
	
	
	
	
	

}
