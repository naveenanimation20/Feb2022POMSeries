package com.qa.opencart.pages;

import org.openqa.selenium.By;

public class CartPage {
	
	private By cart = By.id("page-cart");
	
	public void printCart() {
		System.out.println("cart items");
		System.out.println("click on : "+cart);
	}
	
	
	

}