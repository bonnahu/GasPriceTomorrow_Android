package com.jpeckham.examples.presenters;

import com.jpeckham.examples.views.IAskNameView;

public class AskNamePresenter {
	private IAskNameView _view;
	public AskNamePresenter(IAskNameView view){
		_view = view;
	}
	public void EnterNameAndAge(String name, int age) {
		_view.setDisplayedText(String.format("%s is %d years old",name,age));
	}
}
