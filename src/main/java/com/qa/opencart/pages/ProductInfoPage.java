package com.qa.opencart.pages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.ElementUtil;

public class ProductInfoPage {

	private WebDriver driver;
	private ElementUtil eleUtil;

	private By mainProductName = By.cssSelector("div#content h1");
	private By productImages = By.cssSelector("ul.thumbnails img");
	private By productDescription = By.cssSelector("div#tab-description");
	private By productMetaData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[position()=1]/li");
	private By productPriceData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[position()=2]/li");


	public ProductInfoPage(WebDriver driver) {
		this.driver = driver;
		eleUtil = new ElementUtil(this.driver);
	}

	public String getMainProductName() {
		return eleUtil.doGetElementText(mainProductName);
	}
	
	public int getProductImagesCount() {
		return eleUtil.waitForElementsVisible(productImages, Constants.DEFAULT_ELEMENT_WAIT_TIME_OUT).size();
	}
	
	public String getProductDescription() {
		return eleUtil.doGetElementText(productDescription);
	}
	
	public Map<String, String> getProductInfo() {
		//<k,v>
		Map<String, String> productInfoMap = new HashMap<String, String>();//random order
		//Map<String, String> productInfoMap = new LinkedHashMap<String, String>();//order based
		//Map<String, String> productInfoMap = new TreeMap<String, String>();//sorted order

		productInfoMap.put(null, "TestAutomation");//0
		productInfoMap.put("name", getMainProductName());
		
		//meta data:
//		Brand: Apple
//		Product Code: Product 17
//		Reward Points: 700
//		Availability: In Stock
		List<WebElement> metaList = eleUtil.getElements(productMetaData);
		for(WebElement e : metaList) {
			String metaData = e.getText();
			String metaKey = metaData.split(":")[0].trim();//Product Code
			String metaVal = metaData.split(":")[1].trim();//Product 17

			productInfoMap.put(metaKey, metaVal);
		}
		
		//price data:
//		$1,202.00
//		Ex Tax: $1,000.00
		List<WebElement> priceList = eleUtil.getElements(productPriceData);
		String price = priceList.get(0).getText().trim();
		String exTaxPrice = priceList.get(1).getText().trim();
		
		productInfoMap.put("price", price);
		productInfoMap.put("extaxprice", exTaxPrice);
		
		return productInfoMap;

	}
	
	

}
