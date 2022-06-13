package com.qa.opencart.pages;

import org.openqa.selenium.By;

public class CartPage {

	private By cart = By.id("page-cart");
	private By item = By.id("page-item");

	public void printCart() {
		System.out.println("cart items");
		System.out.println("click on : " + cart);
	}

	public void logo() {

		System.out.println("cart remote logo");
	}

}
