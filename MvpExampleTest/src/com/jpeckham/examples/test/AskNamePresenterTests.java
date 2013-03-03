package com.jpeckham.examples.test;

import com.jpeckham.examples.presenters.AskNamePresenter;
import com.jpeckham.examples.test.fakes.TestingAskNameView;

import junit.framework.TestCase;

public class AskNamePresenterTests extends TestCase {

	public void testFirst()	{
		TestingAskNameView view = new TestingAskNameView();
		AskNamePresenter presenter = new AskNamePresenter(view);
				
		presenter.EnterNameAndAge("James",25);//hah 25 :)
		
		assertEquals("James is 25 years old", 
				view.getTextShownToUser());
	}
}
