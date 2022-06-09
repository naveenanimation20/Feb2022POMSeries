package com.qa.opencart.customexception;

public class FrameworkException extends RuntimeException {

	public FrameworkException(String messg){
		super(messg);
		printStackTrace();
	}

}
