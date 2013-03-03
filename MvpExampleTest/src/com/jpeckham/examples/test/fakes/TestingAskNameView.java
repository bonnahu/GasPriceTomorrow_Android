package com.jpeckham.examples.test.fakes;

import com.jpeckham.examples.views.IAskNameView;

public class TestingAskNameView implements IAskNameView {
	private String _displayedText;
	
	public String getTextShownToUser(){
		return _displayedText;
	}
	@Override
	public void setDisplayedText(String text) {
		_displayedText = text;		
	}

}
