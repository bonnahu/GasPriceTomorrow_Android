package com.jpeckham.examples;

import com.jpeckham.examples.presenters.AskNamePresenter;
import com.jpeckham.examples.views.IAskNameView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class AskNameView extends Activity implements IAskNameView {
    /** Called when the activity is first created. */
	AskNamePresenter _presenter;
	
	public AskNameView(){
		_presenter = new AskNamePresenter(this);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	@Override
	public void setDisplayedText(String text) {
		getShownLabel().setText(text);
	}
	
	public void submitClicked(View widget)	{
		_presenter.EnterNameAndAge(getNameField().getText().toString(), 
				Integer.parseInt(getAgeField().getText().toString()));
	}
	
	private EditText getNameField()	{
		return (EditText) findViewById(R.id.name);
	}
	private EditText getAgeField(){
		return (EditText) findViewById(R.id.age);
	}
	private TextView getShownLabel(){
		return (TextView) findViewById(R.id.shown);
	}
}